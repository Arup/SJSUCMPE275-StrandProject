package OneVillageGroup.repository;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import OneVillageGroup.resource.Event;
import OneVillageGroup.resource.Group;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

@SuppressWarnings({"unchecked"} )
public class GroupClient {
    
    public static final String sCollection = "mongo.collection";
    public static final String sDB = "mongo.db";
    public static final String sHost = "mongo.host";

    private DBCollection collection;
    private Properties props;
    
    public GroupClient()
    {
        Properties conf=new Properties();
        conf.setProperty(GroupClient.sHost, "127.0.0.1:27017");  //localhost  PALN00486285A/192.168.56.1:27017
        conf.setProperty(GroupClient.sDB, "OneVillage");
        conf.setProperty(GroupClient.sCollection, "group_coll");

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
            Mongo m = new Mongo(props.getProperty(sHost));
            DB db = m.getDB(props.getProperty(sDB));
            collection = db.getCollection(props.getProperty(sCollection));
            if (collection == null)
                throw new RuntimeException("Missing collection: " + props.getProperty(sCollection));

            return collection;
        } catch (Exception ex) {
            // should never get here unless no directory is available
            throw new RuntimeException("Unable to connect to mongodb on " + props.getProperty(sHost));
        }
    }
    private List<Group> translateFromMongo(DBCursor cursor) {
        ArrayList<Group> r = new ArrayList<Group>();

        while (cursor.hasNext()) {	
            DBObject data = cursor.next();

            Group b = new Group();
            String v;
            
            v = (String) data.get("groupid");  
            if (v != null)
                b.setGroupid(v);
            
            v = (String) data.get("name");
            if (v != null)
                b.setName(v);
            
            v = (String) data.get("description");
            if (v != null)
                b.setDescription(v);

            
            v = (String) data.get("ownerid");
            if (v != null)
                b.setOwnerid(v);

            v = (String) data.get("timestamp");
            //Date d = (Date) data.get("timestamp");
            //v= d.toString(); 
            if (v != null)
                b.setTimestamp(v);

            // Rajib : are the following 2 really needed? 
            List<String> lv;
            lv = (List<String>) data.get("members");
            if (lv != null) 
                b.setMembers(lv);   
            
            List<Event> evl;
            evl = (List<Event>) data.get("events");
            if (evl != null) 
                b.setEvents(evl);   
            
            
            r.add(b);
            
        }
        
        return r;

    }
    public void addGroup(Group b) {
        try {
            DBCollection col = connect();
            DBObject dob = convertToMongo(b);
            col.insert(dob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public void removeGroup(String groupid, String userid) {
        try {
            DBCollection col = connect();
            BasicDBObject dob = new BasicDBObject();
            dob.append("groupid", groupid);
            dob.append("ownerid", userid);
            col.remove(dob);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMember(String groupid, String userid) {
        try {
            DBCollection col = connect();
            BasicDBObject dobQuery = new BasicDBObject().append("$push", new BasicDBObject().append("members", userid));
    		col.update(new BasicDBObject().append("groupid", groupid), dobQuery);
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }    
    	
    }    
    
    
    public void deleteMember(String groupid, String userid) {
        try {
            DBCollection col = connect();

            BasicDBObject dobQuery = new BasicDBObject().append("$pull", 
    				new BasicDBObject().append("members", userid));
     
    		col.update(new BasicDBObject().append("groupid", groupid), dobQuery);
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }    
    	
    }    
    
    
    public List<Group> getGroup(String id){

        DBCursor dbc = null;
        List<Group> list = null;
        try{
            DBCollection col = connect();
            BasicDBObject dob = new BasicDBObject();
            dob.append("groupid", id);
            dbc=col.find(dob);
            list=translateFromMongo(dbc);
            }catch(Exception e){
                e.printStackTrace();
        }

        return list;
    }
    
    public List<Group> getAllGroups(){
        DBCursor dbc = null;
        List<Group> list = null;
        try{
            DBCollection col = connect();
            dbc=col.find();
            list=translateFromMongo(dbc);
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
        
    }
    
    public List<String> viewMembers(String groupid) {
    	List<String> mlist = null; 
    	GroupClient groupclient = new GroupClient();
    	List<Group> list = groupclient.getGroup(groupid);
    	if (list.size() > 0 )
    	{	
	    	Group g = list.get(0); 
	    	mlist = g.getMembers(); 
    	}

        return mlist;
    }    
    
    
/**
 * convert our objects to mongo objects
 * @param b
 * @return
 */
private DBObject convertToMongo(Group b) {
    BasicDBObject rtn = new BasicDBObject();
    if (b.getGroupid() != null)
        rtn.append("groupid", b.getGroupid());
    if (b.getName() != null)
        rtn.append("title", b.getName());
    if (b.getDescription() != null)
        rtn.append("description", b.getDescription());
    if (b.getOwnerid() != null)
        rtn.append("userid", b.getOwnerid());
    if (b.getTimestamp() != null)
        rtn.append("timestamp", b.getTimestamp());
    if (b.getMembers() != null)
        rtn.append("members", b.getMembers());
    
    return rtn;
}

// Rajib : testing.....
public static void main(String args[])
{
	
	GroupClient groupclient = new GroupClient();
	List<Group> list = groupclient.getGroup("group1");
	Type srcType = new TypeToken<List<Group>>(){}.getType();
	
	Gson gsonObject = new Gson();
	String rtnGroup = gsonObject.toJson(list, srcType);

	System.out.println("---> Your Group ::: " + rtnGroup);
	
	
}





}