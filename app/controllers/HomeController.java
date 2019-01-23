package controllers;

import models.Task;
import play.mvc.*;
import play.libs.Json;
import javax.inject.Inject;
import play.libs.ws.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletionStage;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;
import controllers.routes;
import post.TaskDTO;
import repo.TaskDAO;

import static controllers.RequestController.link;
import static controllers.RequestController.updateLink;
import static controllers.RequestController.generateLink;

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

    /*
    public CompletionStage<Result> testRequests() {
        final Form<Task> boundForm = form.bindFromRequest();
        Task formData = boundForm.get();
        Logger.error("you put this in form: " + formData.name);
        return ws.url("http://localhost:9000/getTasks").get().thenApply(response ->
                ok(response.asJson()));
        //why does this work:
        //play handles CompletionStage<Result> automatically
        //thenApply applies function to CompletionStage, converting it from request to json
    }
    */
    /*public CompletionStage<Result> createTask2() {
        final Form<Task> boundForm = form.bindFromRequest();
        Task formData = boundForm.get();
        return ws.url("http://localhost:9000/createTask")
                .addHeader("Content-Type", "application/json")
                .post(Json.toJson(formData))
                .thenApplyAsync(response -> {
                return ok(views.html.displayTasks.render(Json.stringify(response.asJson()),form));
                }, ec.current());
        //make the connection, adding header and body. convert the CompletionStage<response> to a CompletionStage<Result>
        //this works but
    }
    */
    public Result createTask() {
        final Form<Task> boundForm = form.bindFromRequest();
        Task formData = boundForm.get();
        CompletionStage<WSResponse> response = ws.url("http://localhost:9000/createTask")
                .addHeader("Content-Type", "application/json")
                .post(Json.toJson(formData));
        try {
            response.toCompletableFuture().get();
        }
        catch (Exception e) {
            Logger.error("Failed to finish transaction");
        }
        return redirect(controllers.routes.RequestController.testDisplayTasks());
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
        return redirect(controllers.routes.RequestController.testDisplayTasks());
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
        return redirect(controllers.routes.RequestController.testDisplayTasks());
    }
}
