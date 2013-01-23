package OneVillage.wink.blog.database;


import OneVillage.wink.blog.Article;
import OneVillage.wink.blog.Blog;

import OneVillage.wink.blog.database.BlogArticle;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

public class BlogArticle {
    
    public static final String sCollection = "mongo.collection";
    public static final String sDB = "mongo.db";
    public static final String sHost = "mongo.host";

    private DBCollection collection;
    private Properties props;

    public BlogArticle() {
        Properties conf=new Properties();
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
            Mongo mng = new Mongo(props.getProperty(sHost));
            DB db = mng.getDB(props.getProperty(sDB));
            collection = db.getCollection(props.getProperty(sCollection));
            if (collection == null)
                throw new RuntimeException("Missing collection: " + props.getProperty(sCollection));

            return collection;
        } catch (Exception ex) {
            
            throw new RuntimeException("Connection to mongo db falied on " + props.getProperty(sHost));
        }
    }
    
    public void addArticle(String blogid,Article b) {
    	 DBCursor dbcursor = null;
         List<Blog> list = null;
        try {
            DBCollection dbcollection = connect();
            DBObject dbobject = convertToMongo(b);
            
            dbcollection.insert(dbobject);
            
//            BasicDBObject dob2 = new BasicDBObject();
//            dob2.append("blogid", blogid);
//            dbc=col.find(dob2);
//            
            
//            BasicDBObject dobQuery = new BasicDBObject().append("$push",
//                    new BasicDBObject().append("articles", dob));
//            System.out.println(dobQuery.toString());
//            
//            
//           System.out.println(col.update(new BasicDBObject().append("blogid", blogid), dobQuery).toString());
//      // col.update(new BasicDBObject().append("blogid", blogid), dobQuery);
//       col.update(dob2, dobQuery);
//       
            
         
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
   
    
    public List<Article> getAllArticles(String blogid){
        DBCursor dbcursor = null;
        List<Article> articlelist = null;
        try{
            DBCollection dbcollection = connect();
            BasicDBObject dbobject = new BasicDBObject();
            dbobject.append("blogid", blogid);
            dbcursor=dbcollection.find(dbobject);
            articlelist = translateFromMongo(dbcursor);
        }catch(Exception e){
            e.printStackTrace();
        }
        return articlelist;
        
    }
    public List<Article> getArticle(String blogid,String articleid){

        DBCursor dbcursor = null;
        List<Article> list = null;
        try{
            DBCollection dbcollection = connect();
            BasicDBObject dbobject = new BasicDBObject();
            dbobject.append("blogid", blogid);
            dbobject.append("articleid", articleid);
            dbcursor=dbcollection.find(dbobject);
        
            list=translateFromMongo(dbcursor);
            }catch(Exception e){
                e.printStackTrace();
        }

        return list;
    }
    
    public void removeArticle(String blogid, String articleid, String userid) {
        try {
            DBCollection dbcollection = connect();
            BasicDBObject dbobject = new BasicDBObject();
            dbobject.append("blogid", blogid);
            dbobject.append("articleid", articleid);
            dbobject.append("userid", userid);
            
            dbcollection.findAndRemove(dbobject);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public void remove(String blogid, String userid) {
		// TODO Auto-generated method stub
	try{

				
				BasicDBObject dbquery = new BasicDBObject();
				dbquery.append("blogid", blogid);
				dbquery.append("userid", userid);
			
				DBCollection dbcollection = connect();
				DBCursor element = dbcollection.find(dbquery);
				if(element.hasNext()){
					dbcollection.findAndRemove(element.next());
					System.out.println("Article are also deleted succesfully with blogid: "+blogid+" & userid: "+userid);
				}
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
	}
    public void updateArticle(String blogid, String articleid, String userid, Article article) {
        try {
         
            DBCollection dbcollection = connect();
            BasicDBObject dbobject = new BasicDBObject();
            dbobject.append("blogid", blogid);
            dbobject.append("articleid", articleid);
            dbobject.append("userid", userid);
            
            // New abject to be updated to database
            DBObject newdob = convertToMongo(article);
          
            //Updating the new Object with the Old Object
           
            dbcollection.update(dbobject,newdob);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
   
    private DBObject convertToMongo(Article b) 
    {
        BasicDBObject rtn = new BasicDBObject();
        if (b.getBlogid() != null)
            rtn.append("blogid", b.getBlogid());
        if (b.getArticleid() != null)
            rtn.append("articleid", b.getArticleid());
        if (b.getArticletitle() != null)
            rtn.append("articletitle", b.getArticletitle());        
        if (b.getContent() != null)
            rtn.append("content", b.getContent());
        if (b.getUserid() != null)
            rtn.append("userid", b.getUserid());
        if (b.getTimestamp() != null)
            rtn.append("timestamp", b.getTimestamp());
        
        return rtn;
    }

    
    private List<Article> translateFromMongo(DBCursor cursor) {
        ArrayList<Article> r = new ArrayList<Article>();

        System.out.println("---> The cursor size: " + cursor.size());
        for (int n = 0, N = cursor.size(); n < N; n++) {
            DBObject data = cursor.next();

            Article b = new Article();
            String value = (String) data.get("articleid");
            if (value != null)
                b.setArticleid(value);

            value = (String) data.get("content");
            if (value != null)
                b.setContent(value);
            
            value=(String) data.get("title");
            if (value != null)
                b.setArticletitle(value);
            
            value = (String) data.get("userid");
            if (value != null)
                b.setUserid(value);

            value = (String) data.get("timestamp");
            if (value != null)
                b.setTimestamp(value);

            r.add(b);
        }
        
        return r;

    }

    
}

