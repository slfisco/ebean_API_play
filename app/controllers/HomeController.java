package controllers;

import play.mvc.*;
import repo.personRepo;
import views.html.*;
import javax.inject.Inject;
import play.libs.Scala;
import models.person;

import java.util.List;
import io.ebean.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    private final personRepo repository;

    @Inject
    public HomeController(personRepo repository) {
        this.repository = repository;
    }

    public Result index() {
        List<person> persons = repository.getPersons();
        String firstPersonName = persons.get(0).name;
        return ok(views.html.displayPersons.render(firstPersonName));//asScala(repository.getPersons())));
    }
    public Result rawSQL() {
        String sql
                = "select name from person";
        RawSql rawSql =
                RawSqlBuilder.parse(sql)
                        .columnMapping("name", "name") //map name from sql query to person.name
                        .create();
        Query<person> query = Ebean.find(person.class); //query to apply sql to
        query.setRawSql(rawSql); //execute sql
        List<person> list = query.findList(); //convert results to list
        return ok(views.html.displayPersons.render(list.get(0).name));
    }
    public Result updateSQL() {
        String sql
                = "UPDATE person\n" +
                "SET name = 'new name2'\n" +
                "WHERE id = 1;";
        SqlUpdate update = Ebean.createSqlUpdate(sql);
        update.execute();
        return ok("ok");
    }
    public Result updateEntity(Long id) {
        repository.updatePerson(Long.valueOf(1),"newtest");
        return ok(repository.getPerson(id).name);
    }
}
