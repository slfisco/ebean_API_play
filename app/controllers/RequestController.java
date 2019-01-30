package controllers;

import models.Task;
import play.mvc.*;
import repo.TaskDAO;
import views.html.*;
import javax.inject.Inject;
import play.libs.Scala;
import post.TaskDTO;
import play.libs.Json;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

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
    private final TaskDAO repository;

    private Form<Task> form;

    @Inject
    public RequestController(FormFactory formFactory, TaskDAO repository) {
        this.form = formFactory.form(Task.class);
        this.repository = repository;
    }

    public Result updateEntity(Integer id) { //note ID is not used
        //updateTask should take json from body request, convert it to a Task, pass that task to repo
        //body should have name, id, and isTaskComplete
        JsonNode json = request().body().asJson();
        Task taskFromRequest = Json.fromJson(json, Task.class);
        Task task = repository.updateTask(taskFromRequest);
        return ok(Json.toJson(convertToDTO(task)));
    }

    public Result delete(Integer id) {
        repository.deleteTask(id);
        return noContent();
    }
    public Result getTask(Integer id) {
        Task task = repository.getTask(id);
        return ok(Json.toJson(convertToDTO(task)));
    }

    public Result createTask() {
        //createTask should take json from body request, convert it to a Task, pass that task to repo
        //body should have only name. the rest will be generated. name should not be in url
        JsonNode json = request().body().asJson();
        Task taskFromRequest = Json.fromJson(json, Task.class);
        Task task = repository.createTask(taskFromRequest);
        return ok(Json.toJson(convertToDTO(task)));
    }
    public Result displayTasks() {
        //rewrite to return json only and have homecontroller make http request
        //can then remove form from this controller
        List<Task> tasks = repository.getTasks();
        Collections.sort(tasks, Comparator.comparing(Task::getId));
        String jsonString = Json.stringify(Json.toJson(tasks.stream().map(data -> convertToDTO(data))));
        return ok(views.html.displayTasks.render(jsonString, form));
    }

    public static String generateLink(Task task, String linkType) {
        Http.Request request = Http.Context.current().request();
        return request.host() + "/" + linkType + "/" + task.id; //static
    }
    public static TaskDTO convertToDTO(Task task) {
        return new TaskDTO(task.id,task.name,task.isTaskComplete, generateLink(task,"getTask"), generateLink(task,"updateLink"), generateLink(task,"delete"));
    }

}
