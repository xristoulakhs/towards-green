package com.aueb.towardsgreen.domain;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.aueb.towardsgreen.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Profile {

    private final String QRPATH = "domain/qrCodes";
    private final String CHRASET = "UTF-8";

    private String fullName;
    private int userID;
    private ArrayList<Badge> badges;
    private int points;
    private ROLE role;
    private Bitmap imgBitmap;

    enum ROLE{
        USER,
        SUPERVISOR
    }

    public Profile(){
        this.badges= new ArrayList<>();
        this.points = 0;
        this.role=ROLE.USER;
    }

    public Profile(String fullName, int userID, ArrayList<Badge> badges, int points, ROLE role) {
        this.fullName = fullName;
        this.userID = userID;
        this.badges = badges;
        this.points = points;
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
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

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
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
//        ImageView qr_image = (ImageView) findViewById(R.id.qrimage);
//        qr_image.setImageBitmap(bmp);
        setImgBitmap(bmp);
        //MatrixToImageWriter.writeToPath(matrix,"png", Paths.get(path+userFullName+".png"));
    }

    public void generateUserId() {
        int userid =(int) ((Math.random() * (99999 - 10000)) + 10000); //max 99999  min 10000
        setUserID(userid);
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


