package controllers;

import controllers.Helper;
import models.Account; //testing account
import models.Task;
import repo.AccountDAO;
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
import controllers.RequestController.*;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import io.ebean.*;

/**
 This controller handles httprequests
 */
public class LoginController extends Controller {

    private final AccountDAO accountRepository;
    private final TaskDAO taskRepository; //combine repositories?
    private Form<Account> loginForm;
    private Form<Task> taskForm;

    @Inject
    public LoginController(FormFactory formFactory, AccountDAO accountRepository, TaskDAO taskRepository) {
        this.loginForm = formFactory.form(Account.class);
        this.taskForm = formFactory.form(Task.class);
        this.accountRepository = accountRepository;
        this.taskRepository = taskRepository;
    }

    public Result index() {
        return ok(views.html.index.render(loginForm));
    }

    public Result authenticate() {
        Account formLogin = loginForm.bindFromRequest().get();
        if (accountRepository.accountIsValid(formLogin)) {
            session().clear();
            session("username", formLogin.username);
            Logger.error("account is valid");
            return redirect(routes.LoginController.displayTasks());
        }
        else {
            flash("status", "Authentication error. Please try again");
            return redirect(routes.LoginController.index());
        }
    }
    @Security.Authenticated(Secured.class)
    public Result displayTasks() {
        Account formLogin = loginForm.bindFromRequest().get();
        List<Task> tasks = taskRepository.getTasks();
        Collections.sort(tasks, Comparator.comparing(Task::getId));
        String jsonString = Json.stringify(Json.toJson(tasks.stream().map(data -> Helper.convertToDTO(data))));
        return ok(views.html.displayTasks.render(jsonString, taskForm));
    }
    public Result logOut() {
        session().clear();
        flash("status", "You have been logged out.");
        return redirect(routes.LoginController.index());
    }
}
