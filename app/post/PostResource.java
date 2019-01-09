package post;

public class PostResource {
    private Integer id;
    private String name;
    private Boolean isTaskComplete;

    public PostResource(Integer id, String name, Boolean isTaskComplete) {
        this.id = id;
        this.name = name;
        this.isTaskComplete = isTaskComplete;
    }
    public Integer getId() {return id;}
    public String getName() {return name;}
    public Boolean getIsTaskComplete() {return isTaskComplete;}
}
