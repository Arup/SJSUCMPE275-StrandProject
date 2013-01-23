package OneVillage.wink.blog.database;


import OneVillage.wink.blog.Blog;


import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

public class BlogClient {
    
    public static final String sCollection = "mongo.collection";
    public static final String sDB = "mongo.db";
    public static final String sHost = "mongo.host";

    private DBCollection collection;
    private Properties props;
    
    public BlogClient()
    {
        Properties conf = new Properties();
        conf.setProperty(BlogClient.sHost, "127.0.0.1:27017");  //localhost 
        conf.setProperty(BlogClient.sDB, "OneVillage");
        conf.setProperty(BlogClient.sCollection, "blog");

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
    private List<Blog> translateFromMongo(DBCursor cursor) {
        ArrayList<Blog> r = new ArrayList<Blog>();

        System.out.println("---> cursor size: " + cursor.size());
        for (int n = 0, N = cursor.size(); n < N; n++) {
            DBObject data = cursor.next();

            Blog b = new Blog();
            String value;
            //Date time;
            
            value = (String) data.get("blogid");
            if (value != null)
                b.setBlogid(value);
            
            value = (String) data.get("title");
            if (value != null)
                b.setTitle(value);
            
            value = (String) data.get("description");
            if (value!= null)
                b.setDescription(value);

            
            value = (String) data.get("userid");
            if (value != null)
                b.setUserid(value);

            try
            {
            	value = (String) data.get("timestamp");	
            }
            catch (Exception e){
            	Date d = (Date) data.get("timestamp");
                value = d.toString();             	
            }
            
           //v = time.toString();
            if (value != null)
                b.setTimestamp(value);
            
         
            r.add(b);

        }
        
        return r;

    }
    public void addBlog(Blog b) {
        try {
            DBCollection dbcollection = connect();
            DBObject dbobject = BlogconvertToMongo(b);
            dbcollection.insert(dbobject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public void removeBlog(String blogid, String userid) {
        try {
            DBCollection dbcollection = connect();
            BasicDBObject dbobject = new BasicDBObject();
            dbobject.append("blogid", blogid);
            dbobject.append("userid", userid);
            
         
            //dbcollection.remove(dbobject);
            dbcollection.findAndRemove(dbobject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    
    
    public List<Blog> getBlog(String id){

        DBCursor dbcursor = null;
        List<Blog> list = null;
        try{
            DBCollection dbcollection = connect();
            BasicDBObject dbobject = new BasicDBObject();
            dbobject.append("blogid", id);
            dbcursor = dbcollection.find(dbobject);
            list = translateFromMongo(dbcursor);
            }
        catch(Exception e){
                e.printStackTrace();
        }

        return list;
    }
    public List<Blog> getAllBlogs(){
        DBCursor dbcursor = null;
        List<Blog> list = null;
        try{
            DBCollection dbcollection = connect();
            dbcursor = dbcollection.find();
            list=translateFromMongo(dbcursor);
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
        
    }

    
private DBObject BlogconvertToMongo(Blog b) {
   
	BasicDBObject rtn = new BasicDBObject();
    if (b.getBlogid() != null)
        rtn.append("blogid", b.getBlogid());
    if (b.getTitle() != null)
        rtn.append("title", b.getTitle());
    if (b.getDescription() != null)
        rtn.append("description", b.getDescription());
    if (b.getUserid() != null)
        rtn.append("userid", b.getUserid());
    if (b.getTimestamp() != null)
        rtn.append("timestamp", b.getTimestamp());
    //int listelements;
	if (b.getData() != null)
		 rtn.append("articles", b.getData());
		
//    	//listelements = b.getData().size();
//    	for (int i = 0; i < b.getData().size() ; i++)
//    	{ System.out.println(b.getData().size());
//        rtn.append("articles", b.getData());
//        }
    
    return rtn;
}



}