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

    public boolean loginIsValid(Account account) { //testing account
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
    public accountValidationResult newAccountIsValid(Account account) {
        accountValidationResult result = new accountValidationResult();
        if (ebeanServer.find(Account.class).where().eq("username", account.username).findOne() != null) {
            result.isSuccess = false;
            result.errorMessage = "Error: Username already exists. Please choose another username.";
            return result;
        }
        else if (account.username == "") {
            result.isSuccess = false;
            result.errorMessage = "Error: Username cannot be blank.";
            return result;
        }
        else if (account.password == "") {
            result.isSuccess = false;
            result.errorMessage = "Error: Password cannot be blank.";
            return result;
        }
        else {
            result.isSuccess = true;
            return result;
        }
    }
    //split to different class?
    public class accountValidationResult {
        public boolean isSuccess;
        public String errorMessage;
    }
}
