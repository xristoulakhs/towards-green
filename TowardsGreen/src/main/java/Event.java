import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Event implements Serializable {

	public enum Status {
		OPEN {
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
	private String creatorID;
	/*
	Due to problems during the parsing process (LocalTime and LocalDate
	objects couldn't be resolved and returned an empty object), we will
	use this simple int array technique.
	Date is represented as follows: [YEAR, MONTH, DAY]
	Time is represented as follows: [HOUR, MINUTE]
	 */
	private int[] publishedDate, meetingDate;
	private int[] publishedTime, meetingTime;

	private final int YEAR = 0;
	private final int MONTH = 1;
	private final int DAY = 2;

	private final int HOUR = 0;
	private final int MINUTE = 1;


	private Status status;
	private String title;
	private String description;
	// Image is optional. Image type needs to be changed in Android Studio, so that we can depict it in the device.
	private byte[] image;
	private String meetingLocation;
	// <Reactions, Users that reacted>
	private HashMap<String, ArrayList<String>> reactions;
	// <Requirement, fulfilled or not>
	private HashMap<String, Boolean> requirements;
	// <userID, presence>
	private HashMap<String, Boolean> attendees;
	private HashMap<String, String> attendeesNames;
	private Badge badge;

	public Event(String eventID, String creator, String creatorID, int[] publishedDate, int[] meetingDate,
				 int[] publishedTime, int[] meetingTime, String title, String description,
				 byte[] image, String meetingLocation, HashMap<String, ArrayList<String>> reactions,
				 HashMap<String, Boolean> requirements, HashMap<String, Boolean> attendees,
				 HashMap<String, String> attendeesNames, Badge badge) {
		this.eventID = eventID;
		this.creator = creator;
		this.creatorID = creatorID;
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
		this.attendeesNames = attendeesNames;
		this.badge = badge;
	}

	public Event() {
		this.eventID = UUID.randomUUID().toString();
		this.status = Status.OPEN;
		this.initializeReactions();
		this.requirements = new HashMap<>();
		this.attendees = new HashMap<>();
		this.attendeesNames = new HashMap<>();
	}

	public Event(HashMap<String, Boolean> attendees) {
		this.attendees = attendees;
	}

	public Event(HashMap<String, ArrayList<String>> reactions, HashMap<String,Boolean> attendees,
				 HashMap<String, String> attendeesNames) {
		this.reactions = reactions;
		this.attendees = attendees;
		this.attendeesNames = attendeesNames;
	}

	public String getEventID() {
		return this.eventID;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(String creatorID) {
		this.creatorID = creatorID;
	}

	public int[] getPublishedDate() {
		return this.publishedDate;
	}

	public String getPublishedDateString() {
		return this.publishedDate[DAY] + "/" + this.publishedDate[MONTH] +
				"/" + this.publishedDate[YEAR];
	}

	public void setPublishedDate(int year, int month, int day) {
		this.publishedDate = new int[]{year, month, day};
	}

	public void setPublishedDate(int[] publishedDate) {
		this.publishedDate = publishedDate;
	}

	public int[] getMeetingDate() {
		return this.meetingDate;
	}

	public String getMeetingDateString() {
		return this.meetingDate[DAY] + "/" + this.meetingDate[MONTH] +
				"/" + this.meetingDate[YEAR];
	}

	public void setMeetingDate(int year, int month, int day) {
		this.meetingDate = new int[]{year, month, day};
	}

	public void setMeetingDate(int[] meetingDate) {
		this.meetingDate = meetingDate;
	}

	public int[] getPublishedTime() {
		return this.publishedTime;
	}

	public String getPublishedTimeString() {
		return this.publishedTime[HOUR] + ":" + this.publishedTime[MINUTE];
	}

	public void setPublishedTime(int hour, int minute) {
		this.publishedTime = new int[]{hour, minute};
	}

	public void setPublishedTime(int[] publishedTime) {
		this.publishedTime = publishedTime;
	}

	public int[] getMeetingTime() {
		return this.meetingTime;
	}

	public String getMeetingTimeString() {
		return this.meetingTime[HOUR] + ":" + this.meetingTime[MINUTE];
	}

	public void setMeetingTime(int hour, int minute) {
		this.meetingTime = new int[]{hour, minute};
	}

	public void setMeetingTime(int[] meetingTime) {
		this.meetingTime = meetingTime;
	}

	public Status getStatus() {
		return this.status;
	}

	public String getStatusString() {
		return this.status.toString();
	}

	public String getStatusColor() {
		return this.status.getColor();
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getMeetingLocation() {
		return this.meetingLocation;
	}

	public void setMeetingLocation(String meetingLocation) {
		this.meetingLocation = meetingLocation;
	}

	public HashMap<String, ArrayList<String>> getReactions() {
		return this.reactions;
	}

	public int getTakePartNumberOfReactions() {
		return this.getReactions().get("TakePart").size();
	}

	public int getMaybeNumberOfReactions() {
		return this.getReactions().get("Maybe").size();
	}

	public int getNotInterestedNumberOfReactions() {
		return this.getReactions().get("NotInterested").size();
	}

	public void setReactions(HashMap<String, ArrayList<String>> reactions) {
		this.reactions = reactions;
	}

	public HashMap<String, Boolean> getRequirements() {
		return this.requirements;
	}

	public void setRequirements(HashMap<String, Boolean> requirements) {
		this.requirements = requirements;
	}

	public HashMap<String, Boolean> getAttendees() {
		return this.attendees;
	}

	public void setAttendees(HashMap<String, Boolean> attendees) {
		this.attendees = attendees;
	}

	public HashMap<String, String> getAttendeesNames() {
		return this.attendeesNames;
	}

	public void setAttendeesNames(HashMap<String, String> attendeesNames) {
		this.attendeesNames = attendeesNames;
	}

	public Badge getBadge() {
		return this.badge;
	}

	public void setBadge(Badge badge) {
		this.badge = badge;
	}

	public void initializeReactions() {
		this.reactions = new HashMap<String, ArrayList<String>>();
		this.reactions.put("TakePart", new ArrayList<>());
		this.reactions.put("Maybe", new ArrayList<>());
		this.reactions.put("NotInterested", new ArrayList<>());
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

	public boolean addRequirement(String requirement, boolean req) {
		return this.requirements.put(requirement, req);
	}

	public boolean removeRequirement(String requirement) {
		return this.requirements.remove(requirement);
	}

	public void setRequirementFulfilled(String requirement) {
		this.requirements.put(requirement, true);
	}

	public void addAttendee(String userID, String userName) {
		this.attendees.put(userID, false);
		this.attendeesNames.put(userID, userName);
	}

	public void removeAttendee(String userID) {
		this.attendees.remove(userID);
		this.attendeesNames.remove(userID);
	}

	public void setAttendeePresent(String userID) {
		this.attendees.replace(userID, true);
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
