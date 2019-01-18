package controllers;

import models.Task;
import play.mvc.*;
import play.libs.Json;
import javax.inject.Inject;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;


public class HomeController extends Controller implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private Form<Task> form;

    @Inject
    public HomeController(WSClient ws, FormFactory formFactory) {
        this.ws = ws;
        this.form = formFactory.form(Task.class);
    }
    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> testRequests() {
        final Form<Task> boundForm = form.bindFromRequest();
        Task formData = boundForm.get();
        Logger.error("you put this in form: " + formData.name);
        return ws.url("http://localhost:9000/getTasks").get().thenApply(response ->
                ok(response.asJson()));
        /*why does this work:
        play handles CompletionStage<Result> automatically
        thenApply applies function to CompletionStage, converting it from request to json
        */
    }
    public CompletionStage<Result> createTask() {
        final Form<Task> boundForm = form.bindFromRequest();
        Task formData = boundForm.get();
        return ws.url("http://localhost:9000/createTask")
                .addHeader("Content-Type", "application/json")
                .post(Json.toJson(formData))
                .thenApply(response ->
                ok(response.asJson()));
    }
}
