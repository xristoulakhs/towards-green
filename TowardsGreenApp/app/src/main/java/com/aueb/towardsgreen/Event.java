package com.aueb.towardsgreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Event implements Serializable {

	public enum Status {
		OPEN {
			@NonNull
			@Override
			public String toString() {
				return "Ανοιχτό προς συμμετοχή";
			}

			@Override
			public String getColor() {
				return "#6ADBFF";
			}
		},
		IN_PROGRESS {
			@NonNull
			@Override
			public String toString() {
				return "Σε εξέλιξη";
			}

			@Override
			public String getColor() {
				return "#FFDC60";
			}
		},
		CLOSED {
			@NonNull
			@Override
			public String toString() {
				return "Ολοκληρώθηκε";
			}

			@Override
			public String getColor() {
				return "#32A852";
			}
		};

		public abstract String getColor();
	}

	private String eventID;
	// Creator gets user's ID (userID).
	private String creator;
	private String publishedDate, meetingDate;
	private String publishedTime, meetingTime;
	private Status status;
	private String title;
	private String description;
	// Image is optional. Image type needs to be changed in Android Studio, so that we can depict it in the device.
	private byte[] image;
	private String meetingLocation;
	// <Reactions, numberOfReactions>
	private HashMap<String, Integer> reactions;
	// <Requirement, fulfilled or not>
	private HashMap<String, Boolean> requirements;
	// <userID, presence>
	private HashMap<String, Boolean> attendees;
	private String badge;

	public Event(String eventID, String creator, String publishedDate, String meetingDate,
				 String publishedTime, String meetingTime, String title, String description,
				 byte[] image, String meetingLocation, HashMap<String, Integer> reactions,
				 HashMap<String, Boolean> requirements, HashMap<String, Boolean> attendees, String badge) {
		this.eventID = eventID;
		this.creator = creator;
		this.publishedDate = publishedDate;
		this.meetingDate = meetingDate;
		this.publishedTime = publishedTime;
		this.meetingTime = meetingTime;
		this.status = Status.OPEN;
		this.title = title;
		this.description = description;
		this.image = image;
		this.meetingLocation = meetingLocation;
		this.reactions = reactions;
		this.requirements = requirements;
		this.attendees = attendees;
		this.badge = badge;
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public Event(String creator, String meetingDate, String meetingTime, String title, String description,
				 byte[] image, String meetingLocation, String badge) {
		this.eventID = "e1";
		this.creator = creator;
		this.publishedDate = LocalDate.now().toString();
		this.meetingDate = meetingDate;
		this.publishedTime = LocalTime.now().toString();
		this.meetingTime = meetingTime;
		this.status = Status.OPEN;
		this.title = title;
		this.description = description;
		this.image = image;
		this.meetingLocation = meetingLocation;
		this.initializeReactions();
		this.requirements = new HashMap<String, Boolean>();
		this.attendees = new HashMap<String, Boolean>();
		this.badge = badge;
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public Event(String eventID, String creator) {
		this.eventID = eventID;
		this.creator = creator;
		this.publishedDate = LocalDate.now().toString();
		this.meetingDate = LocalDate.now().toString();
		this.publishedTime = LocalTime.now().toString();
		this.meetingTime = LocalTime.now().toString();
		this.status = Status.OPEN;
		this.title = "dokimi";
		this.description = "This is a test!";
		this.image = null;
		this.meetingLocation = "Dokimi!!";
		this.reactions = null;
		this.requirements = null;
		this.attendees = null;
		this.badge = "b101";
	}

	public String getEventID() {
		return eventID;
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

	public void setPublishedDate(LocalDate publishedDate) {
		this.publishedDate = publishedDate.toString();
	}

	public String getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(LocalDate meetingDate) {
		this.meetingDate = meetingDate.toString();
	}

	public String getPublishedTime() {
		return publishedTime;
	}

	public void setPublishedTime(LocalTime publishedTime) {
		this.publishedTime = publishedTime.toString();
	}

	public String getMeetingTime() {
		return meetingTime;
	}

	public void setMeetingTime(LocalTime meetingTime) {
		this.meetingTime = meetingTime.toString();
	}

	public Status getStatus() {
		return status;
	}

	public String getStatusString() {
		return status.toString();
	}

	public String getStatusColor() {
		return status.getColor();
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getMeetingLocation() {
		return meetingLocation;
	}

	public void setMeetingLocation(String meetingLocation) {
		this.meetingLocation = meetingLocation;
	}

	public HashMap<String, Integer> getReactions() {
		return reactions;
	}

	public void setReactions(HashMap<String, Integer> reactions) {
		this.reactions = reactions;
	}

	public HashMap<String, Boolean> getRequirements() {
		return requirements;
	}

	public void setRequirements(HashMap<String, Boolean> requirements) {
		this.requirements = requirements;
	}

	public HashMap<String, Boolean> getAttendees() {
		return attendees;
	}

	public void setAttendees(HashMap<String, Boolean> attendees) {
		this.attendees = attendees;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public void initializeReactions() {
		this.reactions = new HashMap<String, Integer>();
		this.reactions.put("Reaction 1", 0);
		this.reactions.put("Reaction 2", 0);
		this.reactions.put("Reaction 3", 0);
	}

	public void increaseReaction(String reaction) {
		this.reactions.put(reaction, this.reactions.get(reaction) + 1);
	}

	public void decreaseReaction(String reaction) {
		this.reactions.put(reaction, this.reactions.get(reaction) - 1);
	}

	public boolean addRequirement(String requirement, boolean req) {
		return this.requirements.put(requirement, req);
	}

	public boolean removeRequirement(String requirement) {
		return this.requirements.remove(requirement);
	}

	public void setRequirementFulfilled(String requirement) {
		this.requirements.put(requirement, true);
	}

	public boolean addAttendee(String userID) {
		return this.attendees.put(userID, false);
	}

	public boolean removeAttendee(String userID) {
		return this.attendees.remove(userID);
	}

	public boolean isAttendeePresent(String userID) {
		return this.attendees.get(userID);
	}

	public ArrayList<String> getAttendeesList() {
		ArrayList<String> users = new ArrayList<String>();
		for (String user:this.attendees.keySet()) {
			if (this.attendees.get(user)) {
				users.add(user);
			}
		}
		return users;
	}
}
