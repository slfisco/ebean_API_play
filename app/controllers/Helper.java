package controllers;

import models.Task;
import post.TaskDTO;
import play.mvc.*;

public class Helper {
    public static String generateLink(Task task, String linkType) {
        Http.Request request = Http.Context.current().request();
        String protocol = (request.secure()) ? ("https://") : ("http://");
        return protocol + request.host() + "/" + linkType + "/" + task.id; //static
    }
    public static TaskDTO convertToDTO(Task task) {
        return new TaskDTO(task.id,task.name,task.isTaskComplete, task.accountName, generateLink(task,"getTask"), generateLink(task,"updateLink"), generateLink(task,"delete"));
    }
}
