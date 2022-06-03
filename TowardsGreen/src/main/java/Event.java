import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Event implements Serializable {

	public enum Status {
		OPEN {
			@Override
			public String toString() {
				return "������� ���� ���������";
			}

			@Override
			public String getColor() {
				return "#6ADBFF";
			}
		},
		IN_PROGRESS {
			@Override
			public String toString() {
				return "�� �������";
			}

			@Override
			public String getColor() {
				return "#FFDC60";
			}
		},
		CLOSED {
			@Override
			public String toString() {
				return "������������";
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
	private HashMap<String, ArrayList<String>> reactions;
	// <Requirement, fulfilled or not>
	private HashMap<String, Boolean> requirements;
	// <userID, presence>
	private HashMap<String, Boolean> attendees;
	private String badge;

	public Event(String eventID, String creator, String publishedDate, String meetingDate,
				 String publishedTime, String meetingTime, String title, String description,
				 byte[] image, String meetingLocation, HashMap<String, ArrayList<String>> reactions,
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

	public Event(String eventID, String creator) throws IOException {
		this.eventID = eventID;
		this.creator = creator;
		this.publishedDate = LocalDate.now().toString();
		this.meetingDate = LocalDate.now().toString();
		this.publishedTime = LocalTime.now().toString();
		this.meetingTime = LocalTime.now().toString();
		this.status = Status.OPEN;
		this.title = "�������������";
		this.description = "����� �� �����; �� �� ���������� ������.";
		this.getClass().getResource("image/Forests.png");
		BufferedImage bImage = ImageIO.read(new File("C:\\Users\\apipi\\Documents\\UNI\\towards-green\\TowardsGreen\\src\\main\\java\\image\\Forests.png"));
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ImageIO.write(bImage, "png", bos );
	    byte [] data = bos.toByteArray();
		this.image = data;
		this.meetingLocation = "����";
		this.initializeReactions();
		this.reactions.get("TakePart").add("u101");
		this.requirements = new HashMap<String, Boolean>();
		this.requirements.put("Trees", true);
		this.requirements.put("Trees 3", false);
		this.attendees = null;
		this.badge = null;
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

	public String getMeetingLocation() {
		return meetingLocation;
	}

	public void setMeetingLocation(String meetingLocation) {
		this.meetingLocation = meetingLocation;
	}

	public HashMap<String, ArrayList<String>> getReactions() {
		return reactions;
	}

	public void setReactions(HashMap<String, ArrayList<String>> reactions) {
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
		this.reactions = new HashMap<String, ArrayList<String>>();
		this.reactions.put("TakePart", new ArrayList<String>());
		this.reactions.put("Maybe", new ArrayList<String>());
		this.reactions.put("NotInterested", new ArrayList<String>());
	}
}
