package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.Logger;

public class Secured extends Security.Authenticator {
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.LoginController.index());
    }
}
