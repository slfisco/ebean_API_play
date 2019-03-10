import static org.junit.Assert.*;
import org.junit.*;
import controllers.LoginController;
import play.mvc.*;
import play.mvc.Http.RequestBuilder;
import com.google.common.collect.ImmutableMap;
import static play.test.Helpers.*;
import play.test.*;
import play.Application;
import play.Logger;

import play.db.Database;
import play.db.Databases;
import play.db.evolutions.*;
import play.api.db.evolutions.EvolutionsReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Account;

import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import models.Task;
import play.libs.Json;


public class TestRequestController {
    Database database;

    @Test
    public void task_creates_successfully() {
        Application fakeApp = fakeApplication(inMemoryDatabase());
        database = fakeApp.injector().instanceOf(Database.class);
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
                new Evolution(1,
                        "create table task (" +
                                "id integer auto_increment not null," +
                                "name varchar(255)," +
                                "is_task_complete boolean," +
                                "constraint pk_task primary key (id));",
                        "drop table if exists task cascade;"))
        );
        Logger.error(fakeApp.getClass().getName());
        Helpers.start(fakeApp);
        Task task = new Task();
        task.name = "newTask";
        task.isTaskComplete = false;
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST)
                .uri(controllers.routes.RequestController.createTask().url())
                .bodyJson(Json.toJson(task));
        Result result = route(fakeApp, request);
        Connection connection = database.getConnection();
        try {
            ResultSet resultSet = connection.prepareStatement("select * from task").executeQuery();
            Logger.error("generated resultSet");
            resultSet.next();
            Logger.error(resultSet.getString("name"));
            assertTrue("newTask".equals(resultSet.getString("name")));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void task_updates_successfully() {
        Application fakeApp = fakeApplication(inMemoryDatabase());
        database = fakeApp.injector().instanceOf(Database.class);
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
                new Evolution(1,
                        "create table task (" +
                                "id integer auto_increment not null," +
                                "name varchar(255)," +
                                "is_task_complete boolean," +
                                "constraint pk_task primary key (id));" +
                                "INSERT INTO task(id,name,is_task_complete)" +
                                "VALUES (1,'taskToUpdate',false)",
                        "drop table if exists task cascade;"))
        );
        Logger.error(fakeApp.getClass().getName());
        Helpers.start(fakeApp);
        Task task = new Task();
        task.id = 1;
        task.name = "taskToUpdate";
        task.isTaskComplete = true;
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(PUT)
                .uri(controllers.routes.RequestController.updateEntity(1).url())
                .bodyJson(Json.toJson(task));
        Result result = route(fakeApp, request);
        Connection connection = database.getConnection();
        try {
            ResultSet resultSet = connection.prepareStatement("select * from task").executeQuery();
            Logger.error("generated resultSet");
            resultSet.next();
            Logger.error(Boolean.toString(resultSet.getBoolean("is_task_complete")));
            assertTrue(resultSet.getBoolean("is_task_complete"));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void task_deletes_successfully() {
        Application fakeApp = fakeApplication(inMemoryDatabase());
        database = fakeApp.injector().instanceOf(Database.class);
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
                new Evolution(1,
                        "create table task (" +
                                "id integer auto_increment not null," +
                                "name varchar(255)," +
                                "is_task_complete boolean," +
                                "constraint pk_task primary key (id));" +
                                "INSERT INTO task(id,name,is_task_complete)" +
                                "VALUES (1,'taskToDelete',false),(2,'otherTask',false);",
                        "drop table if exists task cascade;"))
        );
        Logger.error(fakeApp.getClass().getName());
        Helpers.start(fakeApp);
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(DELETE)
                .uri(controllers.routes.RequestController.delete(1).url());
        Result result = route(fakeApp, request);
        Connection connection = database.getConnection();
        try {
            ResultSet resultSet = connection.prepareStatement("select * from task").executeQuery();
            Logger.error("generated resultSet");
            resultSet.next();
            Logger.error("first task is: " + resultSet.getString("name"));
            assertTrue("otherTask".equals(resultSet.getString("name")));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
