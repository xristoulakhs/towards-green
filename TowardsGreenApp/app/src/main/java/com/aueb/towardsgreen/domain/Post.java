package com.aueb.towardsgreen.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Post implements Serializable {

    private String postID;
    private String creator;
    private String creatorID;
    private String publishedDate;
    private String publishedTime;
    private String title;
    private int[] votes;
    private byte[] image;
    private HashMap<Profile, String> comments;
    private String description;
    private String location;

    public Post(){
        this.postID = UUID.randomUUID().toString();
        this.votes= new int[2];
        this.comments= new HashMap<>();
    }

    public Post(String creator, String creatorID, String publishedDate, String publishedTime,
                String title,String location, int[] votes, byte[] image, String desc,
                HashMap<Profile, String> comments) {
        this.creator = creator;
        this.creatorID = creatorID;
        this.publishedDate = publishedDate;
        this.publishedTime = publishedTime;
        this.title = title;
        this.image = image;
        this.location=location;
        this.votes = votes;
        this.description=desc;
        this.comments = comments;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImage() {
        return image;
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

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
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
