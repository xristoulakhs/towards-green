import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class Profile {
    private String firstName;
    private String lastName;
    private String userID;
    private ArrayList<Badge> badges;
    private int points;
    private ROLE role;
    private String password;
    private String email;
    private byte[] image;
    //private Bitmap imgBitmap;

    enum ROLE{
        USER {
            @Override
            public String toString() {
                return "Χρήστης";
            }
        },
        SUPERVISOR {
            @Override
            public String toString() {
                return "Επόπτης";
            }
        }
    }

    public Profile() {
        this.badges= new ArrayList<>();
        this.points = 0;
        this.role=ROLE.USER;
        this.userID = UUID.randomUUID().toString();
    }

    public Profile(String firstName, String lastName, String userID, ArrayList<Badge> badges, int points,
                   ROLE role, String password, String email, byte[] image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.badges = badges;
        this.points = points;
        this.role = role;
        this.password = password;
        this.email = email;
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Badge> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<Badge> badges) {
        this.badges = badges;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public Bitmap getImgBitmap() {
//        return imgBitmap;
//    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}


