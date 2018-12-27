package models;

import io.ebean.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import io.ebean.annotation.Sql;

@Entity
//@Sql
public class person extends Model {
    @Id
    public Long id;
    public String name;
}
