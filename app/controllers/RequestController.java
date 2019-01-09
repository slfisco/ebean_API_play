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
        return ok(Json.toJson(tasks.stream().map(data -> new PostResource(data.id,data.name, data.isTaskComplete))));
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
}
