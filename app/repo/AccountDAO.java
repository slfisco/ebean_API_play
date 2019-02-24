package repo;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Account;

import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;

//should rewrite as interface implementation
public class AccountDAO {
    private final EbeanServer ebeanServer;

    @Inject
    public AccountDAO(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }

    public boolean accountIsValid(Account account) { //testing account
        if (ebeanServer.find(Account.class).where().eq("username", account.username).eq("password", account.password).findOne() != null) {
            return true;
        }
        else {
            return false;//would be good to load from config
        }
    }
}
