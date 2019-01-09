package controllers;

import play.mvc.*;
import play.libs.Json;

public class HomeController extends Controller {
    public Result index() {
        return ok(views.html.index.render());
    }

}
