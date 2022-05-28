import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.imageio.ImageIO;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class Profile {

    private String fullName;
    private String userID;
    private ArrayList<Badge> badges;
    private int points;
    private String role;

    public Profile(){
        this.badges= new ArrayList<>();
    }

    public Profile(String fullName, String userID, ArrayList<Badge> badges, int points, String role) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //static function that creates QR Code
    public static void generateQRcode(String data, String path, String charset, Map map, int height, int width) throws WriterException, IOException {
        //the BitMatrix class represents the 2D matrix of bits
        //MultiFormatWriter is a factory class that finds the appropriate Writer subclass
        // for the BarcodeFormat requested and encodes the barcode with the supplied contents.
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
//        ImageIO.write(matrix, "PNG", new File(path));
    }

    //user-defined method that reads the QR code
    public static String readQRcode(String path, String charset, Map map) throws FileNotFoundException, IOException, NotFoundException
    {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
        Result rslt = new MultiFormatReader().decode(binaryBitmap);
        return rslt.getText();
    }

//    private void createFile(MultimediaFile fileToCreate){
//
//        try (FileOutputStream fos = new FileOutputStream("data/consumer/"+fileToCreate.getMultimediaFileName())) {
//            fos.write(fileToCreate.getMultimediaFileChunk());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String args[]) throws WriterException, IOException, NotFoundException
    {
        //data that we want to store in the QR code
        Profile user= new Profile("antonis", "69", null,0, "user");
        String str= user.getFullName()+"\n"+user.getUserID()+"\n"+user.getRole();
        //path where we want to get QR Code
//        String path = "src/qr.png";
        String path = "src/";
        //Encoding charset to be used
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

        //generates QR code with Low level(L) error correction capability
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        //invoking the user-defined method that creates the QR code
        generateQRcode(str, path, charset, hashMap, 200, 200);//increase or decrease height and width accodingly
        //prints if the QR code is generated
        System.out.println("QR Code created successfully.");

        //read qr
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        //generates QR code with Low level(L) error correction capability
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        System.out.println("Data stored in the QR Code is: \n"+ readQRcode(path, charset, hintMap));
    }

}


