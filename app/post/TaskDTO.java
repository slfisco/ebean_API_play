package post;

public class TaskDTO {
    private Integer id;
    private String name;
    private Boolean isTaskComplete;
    public String link;
    public String updateLink;
    public String deleteLink;

    public TaskDTO(Integer id, String name, Boolean isTaskComplete, String link, String updateLink, String deleteLink) {
        this.id = id;
        this.name = name;
        this.isTaskComplete = isTaskComplete;
        this.link = link;
        this.updateLink = updateLink;
        this.deleteLink = deleteLink;
    }

    public Integer getId() {return id;}
    public String getName() {return name;}
    public Boolean getIsTaskComplete() {return isTaskComplete;}
    public String getLink() {return link;}
    public String getUpdateLink() {return updateLink;}
    public String getDeleteLink() {return deleteLink;}

}
