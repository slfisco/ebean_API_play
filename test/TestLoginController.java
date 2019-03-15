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

public class TestLoginController {
    Database database;

    /*
    @Test
    public void h2dbTest() {
        //creates h2 database and manually applies evolution to it
        database = Databases.inMemory();
        Logger.error(database.getName());
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
                new Evolution(1,
                        "create table account (username varchar(255), password varchar(255)," +
                        "constraint pk_account primary key (username));" +
                        "insert into account (username,password)" +
                        "values ('newuser', 'newpassword');",
                        "drop table if exists account cascade;"))
        );
        Connection connection = database.getConnection();
        try {
            //instead of using evolutions could just do sql statement
            *//*connection.prepareStatement(
                    "create table test (" +
                    "  id                            int not null," +
                    "  name                          varchar(255)," +
                    "  constraint pk_test primary key (id)" +
                    ");" +
                            "insert into test (id,name)" +
                    "values (1, 'doodles');").execute();
            Logger.error("executed successfully");
            *//*

            ResultSet resultSet = connection.prepareStatement("select * from account").executeQuery();
            while (resultSet.next()) {
                Logger.error(resultSet.getString("username"));
                Logger.error(resultSet.getString("password"));
            }
            Logger.error("generated resultSet");

        }
        catch (Exception e) {
            Logger.error(e.getMessage());
        }
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
        assertEquals(1, 1);
    }
    */


    //could use @Before or @BeforeClass to reduce boilerplate
    @Test
    public void goodLogin_redirects_to_tasks() {
        Application fakeApp = fakeApplication(inMemoryDatabase());
        database = fakeApp.injector().instanceOf(Database.class);
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
                new Evolution(1,
                        "create table account (username varchar(255), password varchar(255)," +
                                "constraint pk_account primary key (username));" +
                                "insert into account (username,password)" +
                                "values ('testLoginName', 'pass1');",
                        "drop table if exists account cascade;"))
        );
        Logger.error(fakeApp.getClass().getName());
        Helpers.start(fakeApp);
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri(controllers.routes.LoginController.authenticate().url())
                .bodyForm(ImmutableMap.of("username","testLoginName", "password", "pass1"));
        Result result = route(fakeApp, request);
        Logger.error("displayTasks url: " + controllers.routes.LoginController.displayTasks().url());
        Logger.error("redirectLocation url: " + result.redirectLocation().get());
        assertTrue(controllers.routes.LoginController.displayTasks().url().equals(result.redirectLocation().get()));
    }

    @Test
    public void badLogin_redirects_to_login() {
        Application fakeApp = fakeApplication(inMemoryDatabase());
        database = fakeApp.injector().instanceOf(Database.class);
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
                new Evolution(1,
                        "create table account (username varchar(255), password varchar(255)," +
                                "constraint pk_account primary key (username));",
                        "drop table if exists account cascade;"))
        );
        Logger.error(fakeApp.getClass().getName());
        Helpers.start(fakeApp);
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri(controllers.routes.LoginController.authenticate().url())
                .bodyForm(ImmutableMap.of("username","fakename", "password", "fakepass"));
        Result result = route(fakeApp, request);
        Logger.error("displayTasks url: " + controllers.routes.LoginController.index().url());
        Logger.error("redirectLocation url: " + result.redirectLocation().get());
        assertTrue(controllers.routes.LoginController.index().url().equals(result.redirectLocation().get()));
    }
}
