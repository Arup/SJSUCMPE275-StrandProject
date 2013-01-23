package OneVillageGroup.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class TestClient {

	/**
	 * @param args
	 */

	public static String urlString = "http://localhost:8080/OneVillageGroup";

	public static void main(String[] args) {

		createGroup();
		getGroup();
		getAllGroups();
		//deleteGroup();
		createEvent();
		// deleteEvent();

	}

	// tested OK 
	public static void createGroup() {

		JSONObject jsonobj = new JSONObject();
		jsonobj.put("groupid", "group11");
		jsonobj.put("name", "Group Entry 11");
		jsonobj.put("description", "This is the test Group entry # 11");
		jsonobj.put("userid", "user11");
		jsonobj.put("timestamp", getTimestamp());

		String sendObj = jsonobj.toString();

		RestClient objClient = new RestClient();
		String finalurl = urlString + "/group";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for adding the group");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(sendObj);
		System.out.println(clresponse.getMessage());
		System.out.println("The create method called was succesfull");
		System.out.println(clresponse.getStatusType());

	}

	public static void getGroup() {

		RestClient objClient = new RestClient();
		String finalurl = urlString + "/group/group11";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for adding the group");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get();
		
		System.out.println(clresponse.getMessage());
		System.out.println("The getGroup method called was succesfull");
		System.out.println(clresponse.getStatusType());
	}

	public static void getAllGroups() {

		RestClient objClient = new RestClient();
		String finalurl = urlString + "/group";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for getting all the groups");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get();
		System.out.println(clresponse.getMessage());
		System.out.println("The getAllGroup method called was succesfull");
	}

	public static void deleteGroup() {

		RestClient objClient = new RestClient();
		String finalurl = urlString + "/group/group11/user11";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for deleting the group11");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).delete();
		System.out.println(clresponse.getMessage());
		System.out.println("The deleteGroup method called was succesfull");
	}

	/**
		 * 
		 */
	public static void createEvent() {

		JSONObject jsonobj = new JSONObject();
		jsonobj.put("eventname", "Event - 1");
		jsonobj.put("content", "This is the Event added to the group 19");
		jsonobj.put("userid", "onevillageTest");
		jsonobj.put("timestamp", getTimestamp());

		String sendObj = jsonobj.toString();

		RestClient objClient = new RestClient();
		String finalurl = urlString + "/group/group11/event";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for adding the Event");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(sendObj);
		System.out.println(clresponse.getMessage());
		System.out.println("Client Id:" + clresponse.getStatusCode());
	}

	public static void getAllEvents() {

		RestClient objClient = new RestClient();
		String finalurl = urlString + "/group/group11/event";
		Resource res = objClient.resource(finalurl);
		System.out
				.println("Passing the Url to test for getting all the events");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get();
		System.out.println(clresponse.getMessage());
		System.out.println("The getAllEvent method called was succesfull");
	}

	public static void getEvent() {

		RestClient objClient = new RestClient();
		String finalurl = urlString + "/group/group11/event/event11";
		Resource res = objClient.resource(finalurl);
		System.out
				.println("Passing the Url to test for getting the events event11");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get();
		System.out.println(clresponse.getMessage());
		System.out.println("The getAllEvent method called was succesfull");
	}

	public static void deleteEvent() {

		RestClient objClient = new RestClient();
		String finalurl = urlString
				+ "/group/group11/event/event11/user11";
		Resource res = objClient.resource(finalurl);
		System.out
				.println("Passing the Url to test for deleting the events # event11");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).delete();
		System.out.println(clresponse.getMessage());
		System.out.println("The getAllEvent method called was succesfull");
	}

	public static void updateEvent() {

		JSONObject jsonobj = new JSONObject();
		jsonobj.put("title", "Updated Event - 1");
		jsonobj.put("description",
				"This is the updated event added to the group");
		jsonobj.put("userid", "onevillageTest");
		jsonobj.put("timestamp", getTimestamp());

		String sendObj = jsonobj.toString();

		RestClient objClient = new RestClient();
		String finalurl = urlString
				+ "/group/group11/event/event11/user11";
		Resource res = objClient.resource(finalurl);
		System.out
				.println("Passing the Url to test for updating the event # event11");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(sendObj);

	}

	public static String getTimestamp() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println(sdformat.format(c.getTime()));
		String timestamp = sdformat.format(c.getTime());
		return timestamp;
	}

}
