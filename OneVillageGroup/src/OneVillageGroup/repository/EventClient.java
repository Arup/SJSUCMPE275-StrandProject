package OneVillageGroup.repository;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import OneVillageGroup.resource.Event;
import OneVillageGroup.resource.Group;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;


public class EventClient {
    
    public static final String sCollection = "mongo.collection";
    public static final String sDB = "mongo.db";
    public static final String sHost = "mongo.host";

    private DBCollection collection;
    private Properties props;

    public EventClient() {
        Properties conf=new Properties();
        conf.setProperty(EventClient.sHost, "127.0.0.1:27017");
        conf.setProperty(EventClient.sDB, "OneVillage");
        conf.setProperty(EventClient.sCollection, "group_coll");

        init(conf);
    }
    
    public void init(Properties props)
    {
        this.props = props;
    }
    
    public void release() {
        collection = null;
    }
        
    private DBCollection connect() {
        try {
            if (collection != null && collection.getName() != null)
                return collection;
        } catch (Exception ex) {
            collection = null;
        }

        try {
            Mongo mng = new Mongo(props.getProperty(sHost));
            DB db = mng.getDB(props.getProperty(sDB));
            collection = db.getCollection(props.getProperty(sCollection));
            if (collection == null)
                throw new RuntimeException("Missing collection: " + props.getProperty(sCollection));

            return collection;
        } catch (Exception ex) {
            // should never get here unless no directory is available
            throw new RuntimeException("Unable to connect to mongodb on " + props.getProperty(sHost));
        }
    }
    
    public void addEvent(Event ev, String groupid) {
        try {
            
            DBCollection col = connect();
            DBObject dob = convertToMongo(ev);
            
            BasicDBObject dobQuery = new BasicDBObject().append("$push", 
    				new BasicDBObject().append("events", dob));
     
    		col.update(new BasicDBObject().append("groupid", groupid), dobQuery);            
    		
            
        } catch (Exception e) {

        	e.printStackTrace();
        }
    }
    
    public List<Event> getAllEvents(String groupid){
//        DBCursor dbc = null;
//        List<Event> eventList = null;
//        try{
//            DBCollection col = connect();
//            BasicDBObject dob = new BasicDBObject();
//            dob.append("groupid", groupid);
//            dob.append("events", "1");
//            dbc=col.find(dob);
//            eventList = translateFromMongo(dbc);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return eventList;
    	
    	
    	List<Event> eventList = null;
    	GroupClient groupclient = new GroupClient();
    	List<Group> list = groupclient.getGroup(groupid);
    	if (list.size() > 0 )
    	{	
	    	Group g = list.get(0); 
	    	eventList = g.getEvents(); 
    	}

    	return eventList;  	
        
    }
    
    public Event getEvent(String groupid, String eventid){

        Event ev = null;
        List<Event> eventList = null; 
    	GroupClient groupclient = new GroupClient();
    	List<Group> list = groupclient.getGroup(groupid);
    	if (list.size() > 0 )
    	{	
	    	Group g = list.get(0); 
	    	eventList = g.getEvents(); 
    	}

    	Iterator<Event> levitr = eventList.iterator();   
    	while(levitr.hasNext()) {
    		Event e = levitr.next(); 
    	    if(e.getEventid().equalsIgnoreCase(eventid))
    	    	ev = e; 
    	} 
        return ev;
    }
    
/*
    public List<Event> getEvent(String groupid, String eventid){

        DBCursor dbc = null;
        List<Event> list = null;
        try{
            DBCollection col = connect();
            BasicDBObject dob = new BasicDBObject();
            dob.append("groupid", groupid);
            dob.append("eventid", eventid);
            dbc=col.find(dob);
        
            list=translateFromMongo(dbc);
            }catch(Exception e){
                e.printStackTrace();
        }

        return list;
    }
  */  
    public void removeEvent(String groupid, String eventid, String ownerid) {
        try {
            DBCollection col = connect();
            BasicDBObject dob = new BasicDBObject();
            dob.append("groupid", groupid);
            dob.append("eventid", eventid);
            dob.append("ownerid", ownerid);
            col.remove(dob);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public void updateEvent(String groupid, String eventid, String ownerid, String jsonEvent) {
        try {
        	
            DBCollection col = connect();
            BasicDBObject dob = new BasicDBObject();
            dob.append("groupid", groupid);
            dob.append("eventid", eventid);
            dob.append("ownerid", ownerid);
            
            // JSON string to Java Object
            Gson gson = new Gson();
            Event event = gson.fromJson(jsonEvent, Event.class);; 
            
            // New abject to be updated to database
            DBObject newdob = convertToMongo(event);
          
            //Updating the new Object with the Old Object
            col.update(dob,newdob);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    /**
     * convert our objects to mongo objects
     * @param b
     * @return
     */
    private DBObject convertToMongo(Event b) {
        BasicDBObject rtn = new BasicDBObject();
        if (b.getEventid() != null)
            rtn.append("eventid", b.getEventid());
        if (b.getEventName() != null)
            rtn.append("eventname", b.getEventName());        
        if (b.getVenue() != null)
            rtn.append("venue", b.getVenue());
        if (b.getDescription() != null)
            rtn.append("description", b.getDescription());
        if (b.getOwnerid() != null)
            rtn.append("ownerid", b.getOwnerid());
        if (b.getTimestamp() != null)
            rtn.append("timestamp", b.getTimestamp());
        
        return rtn;
    }

    /**
     * convert from mongo objects to our objects
     * 
     * @param cursor
     * @return
     */
    
    public static List<Event> translateFromMongo(DBCursor cursor) {
        ArrayList<Event> r = new ArrayList<Event>();

//        System.out.println("---> cursor size: " + cursor.size());
//        for (int n = 0, N = cursor.size(); n < N; n++) {
        while (cursor.hasNext()) {	
            DBObject data = cursor.next();

            Event ev = new Event();
            String v = (String) data.get("eventid");
            if (v != null)
                ev.setEventid(v);

            v = (String) data.get("description");
            if (v != null)
                ev.setDescription(v);
            
            v=(String) data.get("eventname");
            if (v != null)
                ev.setEventName(v);
            
            v = (String) data.get("ownerid");
            if (v != null)
                ev.setOwnerid(v);

            v = (String) data.get("timestamp");
            if (v != null)
                ev.setTimestamp(v);

            v = (String) data.get("venue");
            if (v != null)
                ev.setVenue(v);
            
            r.add(ev);
        }
        
        return r;

    }

    private Event translateEventFromMongo(DBCursor cursor) {
        while (cursor.hasNext()) {	
            DBObject data = cursor.next();

            Event ev = new Event();
            String v = (String) data.get("eventid");
            if (v != null)
                ev.setEventid(v);

            v = (String) data.get("description");
            if (v != null)
                ev.setDescription(v);
            
            v=(String) data.get("eventname");
            if (v != null)
                ev.setEventName(v);
            
            v = (String) data.get("ownerid");
            if (v != null)
                ev.setOwnerid(v);

            v = (String) data.get("timestamp");
            if (v != null)
                ev.setTimestamp(v);

            v = (String) data.get("venue");
            if (v != null)
                ev.setVenue(v);
            
            return ev;
        }
		return null;
        

    }

    
}

