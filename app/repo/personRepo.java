package repo;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.person;
import play.db.ebean.EbeanConfig;
import javax.inject.Inject;
import java.util.List;

public class personRepo {
    private final EbeanServer ebeanServer;

    @Inject
    public personRepo(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }
    public List<person> getPersons() {
        return ebeanServer.find(person.class).findList();
    }
    public person getPerson(Long id) {
        person foundPerson = ebeanServer.find(person.class).setId(id).findOne();
        return foundPerson;
    }
    public void updatePerson(Long id, String newName) {
        person personToUpdate = getPerson(id);
        personToUpdate.name = newName;
        personToUpdate.update();
    }
}
