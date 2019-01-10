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
        repository.updateTask(id,"newtest2", true);
        return ok(repository.getTask(id).name);
    }
    public Result list() {
        List<Task> tasks = repository.getTasks();
        /*testing
        Logger.error(tasks.get(0).name);
        List<PostResource> postResourceList = tasks.stream().map(data -> new PostResource(data.id,data.name, data.isTaskComplete, link(data))).collect(Collectors.toList());
        Logger.error(postResourceList.get(0).link);
        end testing*/
        return ok(Json.toJson(tasks.stream().map(data -> new PostResource(data.id,data.name, data.isTaskComplete, link(data), updateLink(data)))));
        //maps list of Task to list of postresources then converts to json
    }
    public Result delete(Integer id) {
        repository.deleteTask(id);
        return noContent();
    }
    public Result getTask(Integer id) {
        return ok(repository.getTask(id).name);
    }
    public Result createTask(String name) {
        return ok(repository.createTask(name).name);
    }
    public Result testDisplayTasks() {
        List<Task> tasks = repository.getTasks();
        String jsonString = Json.stringify(Json.toJson(tasks.stream().map(data -> new PostResource(data.id,data.name, data.isTaskComplete, link(data), updateLink(data)))));
        return ok(views.html.displayTasks.render(jsonString, form));
    }
    public String link(Task task) {
        //to generate link to add to PostResource
        //hardcoding link to check functionality
        Http.Request request = Http.Context.current().request();
        return request.path();
    }
    public String updateLink(Task task) {
        Http.Request request = Http.Context.current().request();
        return request.host();
    }
}
