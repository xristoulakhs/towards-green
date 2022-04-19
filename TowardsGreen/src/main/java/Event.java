import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Event {
	enum Status {
		OPEN,
		IN_PROGRESS,
		CLOSED;
	}

	private String eventID;
	// Creator gets user's ID (userID).
	private String creator;
	private LocalDate publishedDate, meetingDate;
	private LocalTime publishedTime, meetingTime;
	private Status status;
	private String title;
	private String description;
	// Image is optional. Image type needs to be changed in Android Studio, so that we can depict it in the device.
	private BufferedImage image;
	private String meetingLocation;
	// <Reactions, numberOfReactions>
	private HashMap<String, Integer> reactions;
	// <Requirement, fulfilled or not>
	private HashMap<String, Boolean> requirements;
	// <userID, presence>
	private HashMap<String, Boolean> attendees;
	private String badge;
	
	public Event(String eventID, String creator, LocalDate publishedDate, LocalDate meetingDate,
			     LocalTime publishedTime, LocalTime meetingTime, String title, String description,
			     BufferedImage image, String meetingLocation, HashMap<String, Integer> reactions, 
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
	
	public Event(String creator, LocalDate meetingDate, LocalTime meetingTime, String title, String description,
		         BufferedImage image, String meetingLocation, String badge) {
	this.eventID = "e1";
	this.creator = creator;
	this.publishedDate = LocalDate.now();
	this.meetingDate = meetingDate;
	this.publishedTime = LocalTime.now();
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
	
	public Event(String eventID, String creator) {
		this.eventID = eventID;
		this.creator = creator;
		this.publishedDate = null;
		this.meetingDate = null;
		this.publishedTime = null;
		this.meetingTime = null;
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

	public LocalDate getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(LocalDate publishedDate) {
		this.publishedDate = publishedDate;
	}

	public LocalDate getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(LocalDate meetingDate) {
		this.meetingDate = meetingDate;
	}

	public LocalTime getPublishedTime() {
		return publishedTime;
	}

	public void setPublishedTime(LocalTime publishedTime) {
		this.publishedTime = publishedTime;
	}

	public LocalTime getMeetingTime() {
		return meetingTime;
	}

	public void setMeetingTime(LocalTime meetingTime) {
		this.meetingTime = meetingTime;
	}

	public Status getStatus() {
		return status;
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

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
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
