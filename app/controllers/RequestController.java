package controllers;

import models.Task;
import play.mvc.*;
import repo.taskRepo;
import views.html.*;
import javax.inject.Inject;
import play.libs.Scala;
import post.PostResource;
import play.libs.Json;
import play.data.Form;
import play.data.FormFactory;
//for testing
import play.Logger;
import java.util.stream.Collectors;
//end testing
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import io.ebean.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class RequestController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    private final taskRepo repository;

    private Form<Task> form;

    @Inject
    public RequestController(FormFactory formFactory,taskRepo repository) {
        this.form = formFactory.form(Task.class);
        this.repository = repository;
    }

    /*public Result rawSQL() {
        String sql
                = "select name from Task";
        RawSql rawSql =
                RawSqlBuilder.parse(sql)
                        .columnMapping("name", "name") //map name from sql query to Person.name
                        .create();
        Query<Task> query = Ebean.find(Task.class); //query to apply sql to
        query.setRawSql(rawSql); //execute sql
        List<Task> list = query.findList(); //convert results to list
        return ok(views.html.displayPersons.render(list.get(0).name));
    }
    public Result updateSQL() {
        String sql
                = "UPDATE Task\n" +
                "SET name = 'new name2'\n" +
                "WHERE id = 1;";
        SqlUpdate update = Ebean.createSqlUpdate(sql);
        update.execute();
        return ok("ok");
    }
    */
    public Result updateEntity(Integer id) {
        Task task = repository.updateTask(id,"newtest2", true);
        return ok(Json.toJson(new PostResource(task.id,task.name,task.isTaskComplete, link(task), updateLink(task))));
    }
    public Result getTasks() {
        List<Task> tasks = repository.getTasks();
        return ok(Json.toJson(tasks.stream().map(task -> new PostResource(task.id,task.name, task.isTaskComplete, link(task), updateLink(task)))));
        //maps list of Task to list of postresources then converts to json
    }
    public Result delete(Integer id) {
        repository.deleteTask(id);
        return noContent();
    }
    public Result getTask(Integer id) {
        Task task = repository.getTask(id);
        return ok(Json.toJson(new PostResource(task.id,task.name,task.isTaskComplete, link(task), updateLink(task))));
    }
    public Result createTask() {
        //createTask should take json from body request, convert it to a Task, pass that task to repo
        //body should have only name. the rest will be generated. name should not be in url
        JsonNode json = request().body().asJson();
        Task taskFromRequest = Json.fromJson(json, Task.class);
        Task task = repository.createTask(taskFromRequest);
        return ok(Json.toJson(new PostResource(task.id,task.name,task.isTaskComplete, link(task), updateLink(task))));
    }
    public Result testDisplayTasks() { //not using controller.getTasks?
        List<Task> tasks = repository.getTasks();
        String jsonString = Json.stringify(Json.toJson(tasks.stream().map(data -> new PostResource(data.id,data.name, data.isTaskComplete, link(data), updateLink(data)))));
        return ok(views.html.displayTasks.render(jsonString, form));
    }
    public String link(Task task) {
        Http.Request request = Http.Context.current().request();
        return request.host() + "/getTask/" + task.id; //static
    }
    public String updateLink(Task task) {
        Http.Request request = Http.Context.current().request();
        return request.host() + "/update/" + task.id; //static
    }
}
