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
    public void updateTask(Integer id, String newName, Boolean isTaskComplete) {
        Task taskToUpdate = getTask(id);
        taskToUpdate.setName(newName);
        taskToUpdate.setIsTaskComplete(isTaskComplete);
        Ebean.update(taskToUpdate);
    }
    public void deleteTask(Integer id) {
        Task foundTask = ebeanServer.find(Task.class).setId(id).findOne(); //find entity
        Ebean.delete(foundTask); //delete entity
    }
    public Task createTask(String name) {
        Task newTask = new Task();
        newTask.setName(name);
        newTask.setIsTaskComplete(false);
        newTask.save();
        return newTask;
    }
}
