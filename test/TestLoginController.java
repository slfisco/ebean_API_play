import static org.junit.Assert.*;
import org.junit.*;
import controllers.LoginController;
import play.mvc.*;
import play.mvc.Http.RequestBuilder;
import com.google.common.collect.ImmutableMap;
import static play.test.Helpers.*;
import play.test.*;
import play.Application;
import play.Logger;

import play.db.Database;
import play.db.Databases;
import play.db.evolutions.*;
import play.api.db.evolutions.EvolutionsReader;
import java.sql.Connection;
import java.util.Collection;

public class TestLoginController {
    /*Database database;

    @Before
    public void createDatabase() {
        database = Databases.inMemory("test");
                play.api.db.evolutions.EvolutionsReader evolutionsReader = Evolutions.forDefault(new Evolution(
                        1,
                        "create table test (id int not null," +
                                "name varchar(255));" +
                                "INSERT INTO test (id, name)" +
                                "VALUES ('1', 'doodle');",
                        "drop table test;"
                ));
                //Collection<Evolution> evolutionCollection = evolutionsReader.getEvolutions(database.getName());
                Evolutions.applyEvolutions(database, evolutionsReader);
    }

    @Test
    public void dbTest() {
        Connection connection = database.getConnection();
        try {
            Logger.error(connection.prepareStatement("select * from test").executeQuery().getString(1));
        }
        catch (Exception e) {
            Logger.error(e.getMessage());
        }
        assertEquals(1, 1);
    }

    @After
    public void shutdownDatabase() {
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }
    */
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
