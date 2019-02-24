package controllers;

import models.Task;
import play.mvc.*;
import play.libs.Json;
import javax.inject.Inject;
import play.libs.ws.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.concurrent.CompletionStage;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;
import controllers.routes;
import repo.TaskDAO;

public class HomeController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private Form<Task> form;
    private final TaskDAO repository;

    @Inject
    public HomeController(WSClient ws, FormFactory formFactory, TaskDAO repository) {
        this.ws = ws;
        this.form = formFactory.form(Task.class);
        this.repository = repository;
    }

    public Result createTask(String name) {
        Task formData = new Task();
        formData.setName(name);
        formData.setIsTaskComplete(false);
        Logger.error("name from form " + formData.name);
        Http.Request request = Http.Context.current().request();
        String protocol = (request.secure()) ? ("https://") : ("http://");
        String url = protocol + request.host() + request.uri();
        Logger.error("ajax url call: " + url);
        String newUrl = url.substring(0,url.lastIndexOf('/')); //may cause issue if character not found
        Logger.error("createTask url call: " + newUrl);
        CompletionStage<WSResponse> response = ws.url(newUrl)
                .addHeader("Content-Type", "application/json")
                .post(Json.toJson(formData));
            JsonNode json = null;
        try {
            json = response.toCompletableFuture().get().getBody(json());
        }
        catch (Exception e) {
            Logger.error("Failed to finish transaction");
        }
        return ok(json);
    }
    public Result deleteTask(Integer id) {
        Http.Request request = Http.Context.current().request();
        String protocol = (request.secure()) ? ("https://") : ("http://");
        CompletionStage<WSResponse> response = ws.url(protocol + request.host() + request.uri()) //static protocol
                .delete();
        try {
            response.toCompletableFuture().get();
        }
        catch (Exception e) {
            Logger.error("Failed to finish transaction");
        }
        return noContent();
    }
    public Result updateTask(Integer id) {
        Task task = repository.getTask(id);
        boolean newTaskStatus = (task.isTaskComplete == true) ? (false) : (true);
        task.setIsTaskComplete(newTaskStatus);
        Http.Request request = Http.Context.current().request();
        String protocol = (request.secure()) ? ("https://") : ("http://");
        CompletionStage<WSResponse> response = ws.url(protocol + request.host() + request.uri())
                .addHeader("Content-Type", "application/json")
                .put(Json.toJson(task));
        JsonNode json = null;
        try {
            json = response.toCompletableFuture().get().getBody(json());
        }
        catch (Exception e) {
            Logger.error("Failed to finish transaction");
        }
        return ok(json);
    }
}
