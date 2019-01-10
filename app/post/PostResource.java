package post;

public class PostResource {
    private Integer id;
    private String name;
    private Boolean isTaskComplete;
    public String link;
    public String updateLink;

    public PostResource(Integer id, String name, Boolean isTaskComplete, String link, String updateLink) {
        this.id = id;
        this.name = name;
        this.isTaskComplete = isTaskComplete;
        this.link = link;
        this.updateLink = updateLink;
    }
    public Integer getId() {return id;}
    public String getName() {return name;}
    public Boolean getIsTaskComplete() {return isTaskComplete;}
    public String getLink() {return link;}
    public String getUpdateLink() {return updateLink;}

}
