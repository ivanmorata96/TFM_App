package APIAccess;

public class UserContent extends Content{
    private String type;
    private int score;
    private String review;

    public UserContent(String type, int score, String review) {
        super();
        this.type = type;
        this.score = score;
        this.review = review;
    }

    public UserContent(String id, String title, String overview, String poster, String type, int score, String review) {
        super(id, title, overview, poster);
        this.type = type;
        this.score = score;
        this.review = review;
    }

    public UserContent(Content other, String type, int score, String review) {
        super(other);
        this.type = type;
        this.score = score;
        this.review = review;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
