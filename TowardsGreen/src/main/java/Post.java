import java.util.HashMap;

public class Post {

    private Profile creator;
    private String title;
    private int[] votes;
    private HashMap<Profile, String> comments;
    private String description;

    public Post(){
        this.votes= new int[2];
        this.comments= new HashMap<>();
    }

    public Post(Profile creator, String title, int[] votes,String desc, HashMap<Profile, String> comments) {
        this.creator = creator;
        this.title = title;
        this.votes = votes;
        this.description=desc;
        this.comments = comments;
    }

    public Profile getCreator() {
        return creator;
    }

    public void setCreator(Profile creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getVotes() {
        return votes;
    }

    public void setVotes(int[] votes) {
        this.votes = votes;
    }

    public HashMap<Profile, String> getComments() {
        return comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComments(HashMap<Profile, String> comments) {
        this.comments = comments;
    }

    public void addComment(Profile profile, String comment){
        HashMap<Profile, String> commentsMap = getComments();
        commentsMap.put(profile,comment);
        setComments(commentsMap);
    }

    public void vote(int vote){
        int[] voteArray=getVotes();
        voteArray[vote]+=1;
        setVotes(voteArray);
    }
}
