package repo;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Task;
import play.db.ebean.EbeanConfig;
import javax.inject.Inject;
import java.util.List;
import play.mvc.*;

//should rewrite as interface implementation
public class TaskDAO {
    private final EbeanServer ebeanServer;

    @Inject
    public TaskDAO(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }
    public List<Task> getTasks(String accountName) {
        return ebeanServer.find(Task.class).where().eq("account_name", accountName).findList();
    }
    public Task getTask(Integer id) {
        Task foundTask = ebeanServer.find(Task.class).setId(id).findOne();
        return foundTask;
    }
    public Task updateTask(Task task) {
        Ebean.update(task);
        return task;
    }
    public void deleteTask(Integer id) {
        Task foundTask = ebeanServer.find(Task.class).setId(id).findOne(); //find entity
        Ebean.delete(foundTask); //delete entity
    }
    public Task createTask(Task task) {
        task.setIsTaskComplete(false);
        task.save();
        return task;
    }
}
