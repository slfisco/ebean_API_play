import static org.junit.Assert.*;
import org.junit.Test;
import controllers.LoginController;
import play.mvc.*;
import play.mvc.Http.RequestBuilder;
import com.google.common.collect.ImmutableMap;
import static play.test.Helpers.*;
import play.test.*;
import play.Application;
import play.Logger;

public class TestLoginController {
    @Test
    public void goodLogin_redirects_to_tasks() {
        Application fakeApp = fakeApplication();
        Helpers.start(fakeApp);
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri(controllers.routes.LoginController.authenticate().url())
                .bodyForm(ImmutableMap.of("username","name1", "password", "pass1"));
        Result result = route(fakeApp, request);
        Logger.error(controllers.routes.LoginController.displayTasks().url());
        Logger.error(result.redirectLocation().get());
        assertTrue(controllers.routes.LoginController.displayTasks().url().equals(result.redirectLocation().get()));
    }
    @Test
    public void badLogin_redirects_to_login() {
        Application fakeApp = fakeApplication();
        Helpers.start(fakeApp);
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri(controllers.routes.LoginController.authenticate().url())
                .bodyForm(ImmutableMap.of("username","fakename", "password", "fakepass"));
        Result result = route(fakeApp, request);
        Logger.error(controllers.routes.LoginController.index().url());
        Logger.error(result.redirectLocation().get());
        assertTrue(controllers.routes.LoginController.index().url().equals(result.redirectLocation().get()));
    }
}
