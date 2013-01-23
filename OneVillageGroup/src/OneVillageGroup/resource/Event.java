package OneVillageGroup.resource;

public class Event {
	
	private String eventid;
	private String eventname;	
	private String description;
	private String ownerid;
	private String timestamp;
	private String venue;
	
	
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	public String getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(String userid) {
		this.ownerid = userid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public void setEventName(String eventName) {
		this.eventname = eventName;
	}
	public String getEventName() {
		return eventname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	
	@Override
	public String toString() {
	   return "Event [eventid=\" + eventid + \", eventname=\" + eventname + \", " +
	   		"timestamp=\" + timestamp + \", venue=\" + venue + \", " +
	   		"description=\" + description + \"]";
	}

}
