package OneVillageGroup.webservices;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;

import OneVillageGroup.repository.EventClient;
import OneVillageGroup.repository.GroupClient;
import OneVillageGroup.resource.Event;
import OneVillageGroup.resource.Group;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;


@Path("/group")
//@javax.ws.rs.ext.Provider
@SuppressWarnings({"unchecked", "unused", "rawtypes"} )
public class GroupServicesImpl {

	public static GroupServicesImpl service;


	private static Logger logger = Logger.getLogger("GroupServicesImpl.class");

	public synchronized static GroupServicesImpl getInstance() {
		if (service == null) {
			service = new GroupServicesImpl();
		}

		return service;
	}



	// Related Path : http://localhost:8080/OneVillage/group
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createGroup(String JSONObject) {

		// First, Get the ID from the input JSONObject
		String groupid = null; 

		// Testing --- 
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(JSONObject);
			JSONObject jobj = (JSONObject) obj;
			groupid =  (String) jobj.get("groupid") ; 
		} catch (Exception e1) {
			System.out.println("ERROR: Group ID not found in the input " +
					"JSON object for Group."); 
			e1.printStackTrace();
		} 		

		// Uncomment it later .. 
		/*
		try {
			groupid = ServiceHelper.parseJSON(JSONObject, "groupid");
		} catch (Exception e1) {
			System.out.println("ERROR: Group ID not found in the input JSON object for Group."); 
			e1.printStackTrace();
		} 
		 */

		// Then, check if Remote service has to be called
		if(!ServiceHelper.callRemote(groupid))
			// serve Locally
		{

			Group group = null;
			try {
				System.out.println(JSONObject);
				System.out.println("Converting JSON to Group type");
				Gson gsonObject = new Gson();
				group = gsonObject.fromJson(JSONObject, Group.class);
				//String groupid = generateID();
				group.setGroupid(groupid);

			} catch(Exception e){
				return Response.status(400).build();
			}

			GroupClient groupclient = new GroupClient();
			System.out.println("Adding the Group to database");
			groupclient.addGroup(group);
			System.out.println(" !!~ Succesfully Added ~!!");

		} 

		else
			// call remote service to serve request
		{
//			Using Restlet API
//			String uri = "http://arup-onevillage.com/../group/{groupid}";
//			org.restlet.Request request = new org.restlet.Request(Method.POST, uri); 
//			Client client = new Client(Protocol.HTTP); 
//			org.restlet.Response response = client.handle(request); 
//			System.out.println("Response from Remote server : " + response); 
			
			// Using org.apache.wink.client API
			invokeRemoteGroupCreate(JSONObject); 
		}

		return Response.status(200).entity("Group created").build();		

	}


	private void invokeRemoteGroupCreate(String jSONObject) {
		// pass call to remote server to create group
		
		RestClient objClient = new RestClient();
		String remoteURL = "http://localhost:8080/OneVillage/group";
		Resource res = objClient.resource(remoteURL);
		System.out.println("Passing the Url to test for adding the group");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(jSONObject);
		System.out.println(clresponse.getMessage());
		System.out.println("The Remote create method Invokation was succesfull");
		System.out.println(clresponse.getStatusType());
		
	}

	private void invokeRemoteGroupRetrieve(String groupid) {
		// pass call to remote server to create group
		
		String remoteURL = "http://localhost:8080/OneVillage";

		RestClient objClient = new RestClient();
		String finalurl = remoteURL + "/group/" + groupid;
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for adding the group");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get();
		
		System.out.println(clresponse.getMessage());
		System.out.println("The getGroup method called was succesfull");
		System.out.println(clresponse.getStatusType());		
		
	}

	// Related Path : http://localhost:8080/OneVillage/group/
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getAllGroups() 
	{

		GroupClient groupclient = new GroupClient();

		System.out.println("Geting list of all Groups");
		List<OneVillageGroup.resource.Group> list= groupclient.getAllGroups();

		System.out.println("Converting to Json");		

		//		Type srcType = new TypeToken<List<OneVillageGroup.resource.Group>>(){}.getType();
		//		Gson gsonObject = new Gson();
		//		String groupList = gsonObject.toJson(list, srcType);

		JSONArray groupList = new JSONArray();

		Iterator i = list.iterator(); 

		while (i.hasNext()) {
			OneVillageGroup.resource.Group g= (OneVillageGroup.resource.Group)i.next(); 
			JSONObject groupJson = new JSONObject();

			groupJson.put("groupid", g.getGroupid()); 
			groupJson.put("name", g.getName());
			groupJson.put("description", g.getDescription());
			groupJson.put("ownerid", g.getOwnerid());
			groupList.add(groupJson);

		}		

		System.out.println("Returning json Object ");
		return groupList.toString();

	}

	/*Error handling - example:
	@GET 
	@Path("retrieve/{uuid}") 
	public Response retrieveSomething(@PathParam("uuid") String uuid) { 
	    if(uuid == null || uuid.trim().length() == 0) { 
	        return Response.serverError().entity("UUID cannot be blank").build(); 
	    } 
	    Entity entity = service.getById(uuid); 
	    if(entity == null) { 
	        return Response.status(400).entity("Entity not found for UUID: " + uuid).build(); 
	    } 
	    String json = //convert entity to json 
	    return Response.ok(json, MediaType.APPLICATION_JSON).build(); 
	} 
	*/ 	
	
	// Related Path : http://localhost:8080/OneVillage/group/{groupid}
	@Path("/{groupid}")
	@GET 
	@Produces({ MediaType.APPLICATION_JSON })
	public String getGroup(@PathParam("groupid") String groupid) {

		if (groupid == null)
			return Response.status(400).entity("Bad Request-Enter proper GroupID").build().toString(); 

		String rtnGroup = null;  
		System.out.println("Fetching Group : " + groupid); 

		// check if Remote service has to be called
		try
		{
			if(!ServiceHelper.callRemote(groupid))
				// serve Locally
			{

				GroupClient groupClient = new GroupClient();
				List<Group> list = groupClient.getGroup(groupid);
				//			Type srcType = new TypeToken<List<Group>>(){}.getType();
				//			
				//			Gson gsonObject = new Gson();
				//			rtnGroup = gsonObject.toJson(list, srcType);		

				Iterator i = list.iterator(); 
				JSONObject groupJson = new JSONObject();
				while (i.hasNext()) {
					OneVillageGroup.resource.Group g= (OneVillageGroup.resource.Group)i.next(); 
					groupJson.put("groupid", g.getGroupid()); 
					groupJson.put("name", g.getName());
					groupJson.put("description", g.getDescription());
					groupJson.put("ownerid", g.getOwnerid());
				}		

				System.out.println("Returning json Object ");
				rtnGroup = groupJson.toString();

			}
			else
				// call remote service to serve request
			{
				// using RESTLET API 
//				String uri = "http://arup-onevillage.com/../group/{groupid}";
//
//				org.restlet.Request request = new org.restlet.Request(Method.GET, uri); 
//				Client client = new Client(Protocol.HTTP); 
//				org.restlet.Response response = client.handle(request); 
//
//				System.out.println("Response" + response);
				invokeRemoteGroupRetrieve(groupid); 
				
			}

		}
		catch (Exception e) {
			e.printStackTrace();

		}

		//return Response.status(200).entity(rtnGroup.toString()).build();	
		return rtnGroup;

	}


	//	//Related Path : http: //localhost:8080/OneVillage/group/{groupid}/{userid}
	//	@Path("/{groupid}/{userid}")
	//	@DELETE
	//	public Response deleteGroup(String groupid, String userid) {
	//		
	//		
	//		GroupClient groupclient = new GroupClient();
	//		
	//		System.out.println("Removing the group from database");
	//		groupclient.removeGroup(groupid, userid);
	//		
	//		return Response.status(200).entity("Groups Deleted").build();
	//	
	//	}


	//TRY THIS FOR TESTING  -GET- http://localhost:8080/OneVillageGroup/group/groupmember/group1	
	//http://host:port/OneVillage/group/groupmember/{group_id} 
	@Path("/groupmember/{groupid}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getGroupMembers(@PathParam("groupid") String groupid) {


		GroupClient groupclient = new GroupClient();

		System.out.println("Getting list of all Group Members... ");
		List<String> list= groupclient.viewMembers(groupid);

		//		Type srcType = new TypeToken<List<String>>(){}.getType();
		//		
		//		System.out.println("Converting to Json");
		//		Gson gsonObject = new Gson();
		//		String memberList = gsonObject.toJson(list, srcType);

		JSONArray memberList = new JSONArray();
		if( list == null )
			return "{members : [\"No Members found...\"] }";  

		Iterator i = list.iterator(); 

		while (i.hasNext()) {
			String member = i.next().toString(); 
			JSONObject memberJson = new JSONObject();

			memberJson.put("member", member); 
			memberList.add(memberJson);

		}		

		System.out.println("Returning json Object ");
		return memberList.toString();

	}	

	//http://host:port/OneVillage/group/groupmember/{group_id}/{user_id}
	@Path("/groupmember/{groupid}/{userid}")
	@POST
	public Response joinGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid) {

		GroupClient groupclient = new GroupClient();

		System.out.println("Adding user " + userid + " in the group " + groupid);
		groupclient.addMember(groupid, userid);

		return Response.status(200).entity("Membership Added").build();
	}	

	//http://host:port/OneVillage/group/groupmember/{group_id}/{user_id}
	@Path("/groupmember/{groupid}/{userid}")
	@DELETE 
	public Response unjoinGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid) {

		GroupClient groupclient = new GroupClient();

		System.out.println("Removing user " + userid + " from the group " + groupid);
		groupclient.deleteMember(groupid, userid);

		return Response.status(200).entity("Membership Deleted").build();
	}	


	//Related Path : http: //localhost:8080/OneVillage/group/{groupid}/event/
	@Path("/{groupid}/event")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createEvent(@PathParam("groupid") String groupid, String JSONObject) {

		OneVillageGroup.resource.Event event = null;
		try{
			System.out.println(JSONObject);
			Gson gsonObject = new Gson();
			event = gsonObject.fromJson(JSONObject, Event.class);
			System.out.println(" Event passed : " + event.toString());
			// Get event id from the request JSON ... 
			//event.setEventid( event id from req);

		}catch(Exception e){
			return Response.status(400).build();
		}

		EventClient eventclient = new EventClient();
		System.out.println("Adding the event to the Group in the database");
		eventclient.addEvent(event, groupid);
		System.out.println(" !!~ Succesfully Added ~!!");

		return Response.status(200).entity("Event entry created").build();

	}


	//Related Path : http:: //localhost:8080/OneVillage/group/{groupid}/event
	@Path("/{groupid}/event")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getAllEvents(@PathParam("groupid") String groupid ) {

		EventClient eventclient = new EventClient();
		System.out.println("Getting all the events of the Group");

		List<Event> eventList = eventclient.getAllEvents(groupid);

		System.out.println("Converting to JSON using gson");

		JSONArray events = new JSONArray();

		if (eventList != null)
		{
			Iterator i = eventList.iterator(); 

			while (i.hasNext()) {
				BasicDBObject data = (BasicDBObject) i.next();  

				JSONObject eventJson = new JSONObject();

				eventJson.put("eventid", data.get("eventid")); 
				eventJson.put("eventname", data.get("eventname"));
				eventJson.put("description", data.get("description"));
				eventJson.put("ownerid", data.get("ownerid"));
				eventJson.put("venue", data.get("venue"));
				eventJson.put("timestamp", data.get("timestamp"));

				events.add(eventJson);

			}		
		}
		else 
		{
			return "No Events found"; 
		}
		System.out.println("Returning json Object ");
		return events.toString();

	}

	//Related Path : http://localhost:8080/OneVillage/group/{groupid}/event/{eventid}
	@Path("/{groupid}/event/{eventid}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getEvent(@PathParam("groupid") String groupid, @PathParam("eventid") String eventid) {

		EventClient eventclient = new EventClient();
		Event event = eventclient.getEvent(groupid, eventid);

		JSONObject eventJson = new JSONObject();
		eventJson.put("eventid", event.getEventid()); 
		eventJson.put("eventname", event.getEventName());
		eventJson.put("description", event.getDescription());
		eventJson.put("ownerid", event.getOwnerid());
		eventJson.put("venue", event.getVenue());
		eventJson.put("timestamp", event.getTimestamp());		


		return eventJson.toString();

	}

	// for the Most basic & Simplest check ...
	// Related Path : http://localhost:8080/OneVillage/group/about
	@Path("/about")
	@GET
	public String about() {

		return "Hello  .... " ;
	}



	//	//Related Path : http: //localhost:8080/OneVillage/group/{groupid}/event/{eventid}/{userid}
	//	@Path("/{groupid}/event/{eventid}/{userid}")
	//	@POST
	//	@Consumes({ MediaType.APPLICATION_JSON })
	//	public Response deleteEvent(String groupid, String eventid, String userid) {
	//		EventClient eventclient = new EventClient();
	//		
	//		System.out.println("Removing Event " + eventid + " from the group " + groupid);
	//		eventclient.removeEvent(groupid, eventid, userid); 
	//
	//		return Response.status(200).entity("Event Deleted").build();
	//	}
	//
	//
	//
	//	//Related Path : http://localhost:8080/OneVillage/group/{groupid}/event/{eventid}/{userid}
	//	@Path("/{groupid}/event/{eventid}/{userid}")
	//	@POST
	//	@Consumes({ MediaType.APPLICATION_JSON })
	//	public Response updateEvent(String groupid, String eventid, String userid, String JSONObject) 
	//	{
	//		EventClient eventclient = new EventClient();
	//		
	//		System.out.println("Updating Event " + eventid + " in the group " + groupid);
	//		eventclient.updateEvent(groupid, eventid, userid, JSONObject); 
	//
	//		return Response.status(200).entity("Event Updated").build();
	//	}	


	public static synchronized String generateID()
	{return String.valueOf(UUID.randomUUID());

	}

	// Rajib : testing 

	public static void main(String args[])
	{

		GroupServicesImpl groupServer = GroupServicesImpl.getInstance(); 
		//		// testing create 1 group
		//		String newGroup =  "{ groupid: \"group1\", name: \"Group 1\", description: \"this is group 1 in One Village\", timestamp: \"2011-01-20\", ownerid: \"user1\", members: [ \"user1\", \"user2\" ], events: [{ eventid: \"event1\",  name: \"event 1\",  description: \"this is event 1 in One Village\",  ownerid: \"user1\",  timestamp: \"2011-01-20\",  venue: \"SJSU\"  },  { eventid: \"event3\",  name: \"event 3\",  description: \"this is event 3 in One Village\",  ownerid: \"user1\",  timestamp: \"2011-04-18\",  venue: \"SJSU\"  }]}" ;  
		//		Response rs = groupServer.createGroup(newGroup); 
		//		System.out.println("---> Your Group ::: " + rs);

		// testing getting all groups
		//		String group = groupServer.getGroup("group1"); 
		//		System.out.println("---> Your Group ::: " + group);
		String groups = groupServer.getAllGroups(); 
		System.out.println("---> Your Groups ::: " + groups);

		//testing getting all members
		//		String groups = groupServer.getGroupMembers("group1"); 
		//		System.out.println("---> Members ::: " + groups);		

		// testing adding/removing a member to the group
		//		String groupid =  "group1"; 
		//		String userid =  "user4";		
		//		//Response rs = groupServer.joinGroup(groupid, userid); 
		//		Response rs = groupServer.unjoinGroup(groupid, userid);
		//		System.out.println("---> Your Membership ::: " + rs);

		// testing create new event in the group
		//		String newEvent =  "{ eventid: \"event5\",  eventname: \"event 5\",  description: \"this is event 5 in One Village\",  ownerid: \"user1\",  timestamp: \"2011-01-20\",  venue: \"SJSU\"  }" ;  
		//		Response rs = groupServer.createEvent("group1", "user1", newEvent);  
		//		System.out.println("---> Your Event ::: " + rs);

		//testing getting all events
		//		String events = groupServer.getAllEvents("group1"); 
		//		System.out.println("---> Events ::: " + events);			

		//testing getting details of one event
		//		String event = groupServer.getEvent("group1", "event5"); 
		//		System.out.println("---> Event ::: " + event);	
		//		logger.debug("Log message : Getting Event" + event );

	}


}
