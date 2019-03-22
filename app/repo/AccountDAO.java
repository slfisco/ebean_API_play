package repo;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Account;
import org.mindrot.jbcrypt.BCrypt;
import play.db.ebean.EbeanConfig;
import play.Logger;
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
        Account foundAccount = ebeanServer.find(Account.class).where().eq("username", account.username).findOne();
        if (foundAccount != null && BCrypt.checkpw(account.password, foundAccount.password)) {
            return true;
        }
        else {
            return false;//would be good to load from config
        }
    }
    public void createAccount(Account account) {
        account.setPassword(BCrypt.hashpw(account.password, BCrypt.gensalt()));
        account.save();
    }
    public boolean usernameIsUnique(Account account) {
        if (ebeanServer.find(Account.class).where().eq("username", account.username).findOne() == null) {
            return true;
        }
        else {
            return false;
        }
    }
    //add method to check if username already exists. redirect and flash if so
}
