public class Badge {

    private String title;
    private int pointsEarned;

    public Badge() {
    }

    public Badge(String title, int pointsEarned) {
        this.title = title;
        this.pointsEarned = pointsEarned;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
}
