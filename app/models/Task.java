package models;

import io.ebean.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import io.ebean.annotation.Sql;

@Entity
//@Sql
public class Task extends Model {
    @Id
    public Integer id;
    public String name;
    public Boolean isTaskComplete;
    public String accountName;
    public void setName(String name) {
        this.name = name;
    }
    public void setIsTaskComplete(Boolean isTaskComplete) { this.isTaskComplete = isTaskComplete;}
    public void setAccountName(String accountName) { this.accountName = accountName;}
    public Integer getId() { return id; }
}
