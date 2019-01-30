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
import play.libs.concurrent.HttpExecutionContext;
import controllers.routes;
import repo.TaskDAO;

public class HomeController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private Form<Task> form;
    private HttpExecutionContext ec;
    private final TaskDAO repository;

    @Inject
    public HomeController(WSClient ws, FormFactory formFactory, HttpExecutionContext ec, TaskDAO repository) {
        this.ws = ws;
        this.form = formFactory.form(Task.class);
        this.ec = ec;
        this.repository = repository;
    }
    public Result index() {
        return ok(views.html.index.render());
    }

    public Result createTask() {
        final Form<Task> boundForm = form.bindFromRequest();
        Task formData = boundForm.get();
        Http.Request request = Http.Context.current().request();
        CompletionStage<WSResponse> response = ws.url("http://" + request.host() + request.uri())
                .addHeader("Content-Type", "application/json")
                .post(Json.toJson(formData));
        try {
            response.toCompletableFuture().get();
        }
        catch (Exception e) {
            Logger.error("Failed to finish transaction");
        }
        return redirect(controllers.routes.RequestController.displayTasks());
    }
    public Result deleteTask(Integer id) {
        Http.Request request = Http.Context.current().request();
        CompletionStage<WSResponse> response = ws.url("http://" + request.host() + request.uri()) //static protocol
                .delete();
        try {
            response.toCompletableFuture().get();
        }
        catch (Exception e) {
            Logger.error("Failed to finish transaction");
        }
        return redirect(controllers.routes.RequestController.displayTasks());
    }
    public Result updateTask(Integer id) {
        Task task = repository.getTask(id);
        boolean newTaskStatus = (task.isTaskComplete == true) ? (false) : (true);
        task.setIsTaskComplete(newTaskStatus);
        Http.Request request = Http.Context.current().request();
        CompletionStage<WSResponse> response = ws.url("http://" + request.host() + request.uri()) //static protocol
                .addHeader("Content-Type", "application/json")
                .put(Json.toJson(task));
        try {
            response.toCompletableFuture().get();
        }
        catch (Exception e) {
            Logger.error("Failed to finish transaction");
        }
        return redirect(controllers.routes.RequestController.displayTasks());
    }

    /*
    //generic httpRequest to reduce redundancy
    public void httpRequest(Task task, String requestType) {


        Http.Request request = Http.Context.current().request();
        CompletionStage<WSResponse> response = ws.url("http://" + request.host() + request.uri()) //static protocol
                .addHeader("Content-Type", "application/json"); //not sure if adding these to deleteTask will do anything

            response.put(Json.toJson(task));
        try {
            response.toCompletableFuture().get();
        }
        catch (Exception e) {
            Logger.error("Failed to finish transaction");
        }
        return redirect(controllers.routes.RequestController.displayTasks());
    }
    public void httpRequest(String requestType) {
    }
*/
}
