package models;

import io.ebean.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.mindrot.jbcrypt.BCrypt;

@Entity
//@Sql
public class Account extends Model {
    @Id
    public String username; //may want to add integer as primary key
    public String password;
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}
}