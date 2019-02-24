package models;

import io.ebean.*;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
//@Sql
public class Account extends Model {
    @Id
    public String username; //may want to add integer as primary key
    public String password;
}