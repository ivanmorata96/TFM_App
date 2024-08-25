package com.main.tfm.APIAccess;

public class UserContent extends Content{
    private String type;
    private int score;
    private String review;
    private String status;

    public UserContent() {
        super();
        type = review = status = "";
        score = 0;
    }

    public UserContent(String id, String title, String overview, String poster, String type, int score, String review, String status) {
        super(id, title, overview, poster);
        this.type = type;
        this.score = score;
        this.review = review;
        this.status = status;
    }

    public UserContent(Content other, String type, int score, String review) {
        super(other);
        this.type = type;
        this.score = score;
        this.review = review;
    }

    public UserContent(UserContent other){
        this.setId(other.getId());
        this.setTitle(other.getTitle());
        this.setOverview(other.getOverview());
        this.setPoster(other.getPoster());
        this.type = other.type;
        this.score = other.score;
        this.review = other.review;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString() + "\nUserContent{" +
                "type='" + type + '\'' +
                ", score=" + score +
                ", review='" + review + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
