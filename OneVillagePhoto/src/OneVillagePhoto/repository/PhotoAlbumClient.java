package OneVillagePhoto.repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import OneVillagePhoto.resource.Album;
import OneVillagePhoto.resource.Photo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class PhotoAlbumClient {

	public static final String sCollection = "mongo.collection";
	public static final String sDB = "mongo.db";
	public static final String sHost = "mongo.host";

	private DBCollection collection;
	private Properties props;

	public PhotoAlbumClient()
	{
		Properties conf=new Properties();
		conf.setProperty(PhotoAlbumClient.sHost, "127.0.0.1:27017");  //localhost  PALN00486285A/192.168.56.1:27017
		conf.setProperty(PhotoAlbumClient.sDB, "OneVillage");
		conf.setProperty(PhotoAlbumClient.sCollection, "photo_album");

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
	private List<Album> translateFromMongo(DBCursor cursor) {
		ArrayList<Album> r = new ArrayList<Album>();

		System.out.println("---> cursor size: " + cursor.size());

		for (int n = 0, N = cursor.size(); n < N; n++) {
			DBObject data = cursor.next();

			Album b = new Album();
			String v;

			v = (String) data.get("albumid");
			if (v != null)
				b.setAlbumid(v);

			v = (String) data.get("title");
			if (v != null)
				b.setTitle(v);

			v = (String) data.get("description");
			if (v != null)
				b.setDescription(v);


			v = (String) data.get("userid");
			if (v != null)
				b.setUserid(v);

			v = (String) data.get("timestamp");
			//Date d = (Date) data.get("timestamp");
			//v= d.toString(); 
			if (v != null)
				b.setTimestamp(v);

			///   IS IT NEEDED ????    
			List<Photo> lv;
			lv = (List<Photo>) data.get("photos");
			if (lv != null) 
				b.setPhotos(lv);               

			r.add(b);

		}

		return r;

	}
	public void addAlbum(Album b) {
		try {
			DBCollection col = connect();
			DBObject dob = convertToMongo(b);
			/*
			 * db.seq.findAndModify({ 
  query: {"_id": "photo_album"}, 
  update: {$inc: {"seq":1}}, 
  new: true 
}); 
			 * 
			 */
			col.insert(dob);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void removeAlbum(String albumid, String userid) {
		try {
			DBCollection col = connect();
			BasicDBObject dob = new BasicDBObject();
			dob.append("albumid", albumid);
			dob.append("userid", userid);
			col.remove(dob);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	public List<Album> getAlbum(String id){

		DBCursor dbc = null;
		List<Album> list = null;
		try{
			DBCollection col = connect();
			BasicDBObject dob = new BasicDBObject();
			// testing wild card search try... 
			//id = "/.*" + id + ".*/"; 
			dob.append("albumid", id);
			dbc=col.find(dob);
			list=translateFromMongo(dbc);
		}catch(Exception e){
			e.printStackTrace();
		}

		return list;
	}

	public List<Album> getAllAlbums(){
		DBCursor dbc = null;
		List<Album> list = null;
		try{
			DBCollection col = connect();
			dbc=col.find();
			list=translateFromMongo(dbc);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;

	}

	public List<Photo> viewAllPhotos(String id){
		DBCursor dbc = null;
		PhotoAlbumClient albumclient = new PhotoAlbumClient();
		List<Photo> plist = null;
		List<Album> alist = albumclient.getAlbum(id);
		try{
			DBCollection col = connect();
			dbc=col.find();
			alist=translateFromMongo(dbc);
			if(alist.size() >0){
				Iterator itr = alist.iterator();
				while(itr.hasNext()) {
					Album a = (Album) itr.next();

					if(a.getAlbumid().equalsIgnoreCase(id))
						if (a.getPhotos() != null)
							plist = a.getPhotos(); 
				}    
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return plist;

	}    

	/**
	 * convert our objects to mongo objects
	 * @param b
	 * @return
	 */
	 private DBObject convertToMongo(Album b) {
		BasicDBObject rtn = new BasicDBObject();
		if (b.getAlbumid() != null)
			rtn.append("albumid", b.getAlbumid());
		if (b.getTitle() != null)
			rtn.append("title", b.getTitle());
		if (b.getDescription() != null)
			rtn.append("description", b.getDescription());
		if (b.getUserid() != null)
			rtn.append("userid", b.getUserid());
		if (b.getTimestamp() != null)
			rtn.append("timestamp", b.getTimestamp());
		if (b.getPhotos() != null)
			rtn.append("photos", b.getPhotos());    

		return rtn;
	 }

	 public void addPhotoToAlbum(String albumid, Photo photo) {

		 
	        try {
	            
	            DBCollection col = connect();
	            DBObject dob = PhotoClient.convertToMongo(photo);
	            
	            BasicDBObject dobQuery = new BasicDBObject().append("$push", 
	    				new BasicDBObject().append("photos", dob));
	     
	    		col.update(new BasicDBObject().append("albumid", albumid), dobQuery);            
	    		
	            
	        } catch (Exception e) {

	        	e.printStackTrace();
	        }
		 

	 }

	 
	 // testing by RS.....
	 public static void main(String args[])
	 {

		 PhotoAlbumClient albumclient = new PhotoAlbumClient();
		 List<Album> list = albumclient.getAlbum("album1"); 
		 Type srcType = new TypeToken<List<Album>>(){}.getType();

		 Gson gsonObject = new Gson();
		 String rtnAlbum = gsonObject.toJson(list, srcType);

		 System.out.println("---> Your Album ::: " + rtnAlbum);


	 }



}
