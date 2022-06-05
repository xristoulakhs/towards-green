package com.aueb.towardsgreen.domain;

import java.io.Serializable;
import java.util.UUID;

public class Badge implements Serializable {

    private String title;
    private String badgeID;
    private String publishedDate;
    private int pointsEarned;

    public Badge() {
    }

    public Badge(String title, String badgeID, String publishedDate, int pointsEarned) {
        this.title = title;
        this.badgeID = badgeID;
        this.publishedDate = publishedDate;
        this.pointsEarned = pointsEarned;
    }

    public Badge(String title, String publishedDate, int pointsEarned) {
        this.title = title;
        this.badgeID = UUID.randomUUID().toString();
        this.publishedDate = publishedDate;
        this.pointsEarned = pointsEarned;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBadgeID() {
        return badgeID;
    }

    public void setBadgeID(String badgeID) {
        this.badgeID = badgeID;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
}
