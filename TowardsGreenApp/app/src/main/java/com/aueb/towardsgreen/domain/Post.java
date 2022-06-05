package com.aueb.towardsgreen.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Post implements Serializable {

    private String postID;
    private String creator;
    private String creatorID;
    private String publishedDate;
    private String publishedTime;
    private String title;
    private HashMap<String, ArrayList<String>> reactions;
    private byte[] image;
    private String description;
    private String location;
    private HashMap<String, String> usersAndReactions;

    public Post(){
        this.postID = UUID.randomUUID().toString();
        initializeReactions();
        usersAndReactions = new HashMap<>();
    }

    public Post(String creator, String creatorID, String publishedDate, String publishedTime,
                String title, String location, byte[] image, String desc,
                HashMap<String, ArrayList<String>> reactions,
                HashMap<String, String> usersAndReactions) {
        this.creator = creator;
        this.creatorID = creatorID;
        this.publishedDate = publishedDate;
        this.publishedTime = publishedTime;
        this.title = title;
        this.image = image;
        this.location=location;
        this.description=desc;
        this.reactions =reactions;
        this.usersAndReactions = usersAndReactions;
    }

    public Post(HashMap<String,ArrayList<String>> reactions,
                    HashMap<String, String> usersAndReactions){
        this.reactions = reactions;
        this.usersAndReactions = usersAndReactions;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public HashMap<String, ArrayList<String>> getReactions() {
        return reactions;
    }

    public HashMap<String, String> getUsersAndReactions() {
        return usersAndReactions;
    }

    public void setUsersAndReactions(HashMap<String, String> usersAndReactions) {
        this.usersAndReactions = usersAndReactions;
    }

    public int getAgreeNumberOfReactions() {
        return this.getReactions().get("Agree").size();
    }
    public int getDisagreeNumberOfReactions() {
        return this.getReactions().get("Disagree").size();
    }

    public void setReactions(HashMap<String, ArrayList<String>> reactions) {
        this.reactions = reactions;
    }

        public void initializeReactions() {
        this.reactions = new HashMap<String, ArrayList<String>>();
        this.reactions.put("Agree", new ArrayList<>());
        this.reactions.put("Disagree", new ArrayList<>());
    }

    public boolean hasReacted(String reaction, String userID) {
        if (this.getReactions().get(reaction).contains(userID)) {
            return true;
        }
        return false;
    }

    public void addReaction(String reaction, String userID) {
        this.getReactions().get(reaction).add(userID);
    }

    public void removeReaction(String reaction, String userID) {
        this.getReactions().get(reaction).remove(userID);
    }
}
