package com.aueb.towardsgreen.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public enum ROLE{
        USER {
            @NonNull
            @Override
            public String toString() {
                return "Χρήστης";
            }
        },
        SUPERVISOR {
            @NonNull
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

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setImage(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.image = stream.toByteArray();
        image.recycle();
    }

    public Bitmap getImageBitmap() {
        return BitmapFactory.decodeByteArray(this.image, 0, this.image.length);
    }

    public String getFullName(){
        return this.getFirstName()+" "+this.getLastName();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void generateQR() {
        try {
            generateQRcode(this.userID, "UTF-8", 200, 200);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    //static function that creates QR Code
    @RequiresApi(api = Build.VERSION_CODES.O) //gia to Paths.get
    public void generateQRcode(String data, String charset, int height, int width) throws WriterException, IOException {
        //the BitMatrix class represents the 2D matrix of bits
        //MultiFormatWriter is a factory class that finds the appropriate Writer subclass
        // for the BarcodeFormat requested and encodes the barcode with the supplied contents.
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);

        int height1 = matrix.getHeight();
        int width1 = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        setImage(bmp);
    }

//    public static void main(String args[]) throws WriterException, IOException, NotFoundException
//    {
//        //data that we want to store in the QR code
//        Profile user= new Profile("antonis", 69, null,0, ROLE.USER);
//        String str= user.getFullName()+"\n"+user.getUserID()+"\n"+user.getRole();
//        //path where we want to get QR Code
//        String path = "com/aueb/towardsgreen/domain/qrCodes";
//        //Encoding charset to be used
//        String charset = "UTF-8";
//        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//
//        //generates QR code with Low level(L) error correction capability
//        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//        //invoking the user-defined method that creates the QR code
//        generateQRcode(str, path, charset, 200, 200);//increase or decrease height and width accodingly
//        //prints if the QR code is generated
//        System.out.println("QR Code created successfully.");
//
//    }

}


