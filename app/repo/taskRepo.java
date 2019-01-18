package repo;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Task;
import play.db.ebean.EbeanConfig;
import javax.inject.Inject;
import java.util.List;

public class taskRepo {
    private final EbeanServer ebeanServer;

    @Inject
    public taskRepo(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }
    public List<Task> getTasks() {
        return ebeanServer.find(Task.class).findList();
    }
    public Task getTask(Integer id) {
        Task foundTask = ebeanServer.find(Task.class).setId(id).findOne();
        return foundTask;
    }
    public Task updateTask(Integer id, String newName, Boolean isTaskComplete) {
        Task task = getTask(id);
        task.setName(newName);
        task.setIsTaskComplete(isTaskComplete);
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
