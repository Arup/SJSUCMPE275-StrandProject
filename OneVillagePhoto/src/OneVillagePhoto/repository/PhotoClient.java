package OneVillagePhoto.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import OneVillagePhoto.resource.Photo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class PhotoClient {

	public static final String sCollection = "mongo.collection";
	public static final String sDB = "mongo.db";
	public static final String sHost = "mongo.host";

	private DBCollection collection;
	private Properties props;

	public PhotoClient()
	{
		Properties conf=new Properties();
		conf.setProperty(PhotoClient.sHost, "127.0.0.1:27017");  //localhost  PALN00486285A/192.168.56.1:27017
		conf.setProperty(PhotoClient.sDB, "OneVillage");
		conf.setProperty(PhotoClient.sCollection, "photo");

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
	private List<Photo> translateFromMongo(DBCursor cursor) {
		ArrayList<Photo> r = new ArrayList<Photo>();

		System.out.println("---> cursor size: " + cursor.size());

		for (int n = 0, N = cursor.size(); n < N; n++) {
			DBObject data = cursor.next();

			Photo b = new Photo();
			String v;

			v = (String) data.get("photoid");
			if (v != null)
				b.setPhotoid(v);

			v = (String) data.get("url");
			if (v != null)
				b.setUrl(v);

			v = (String) data.get("filename");
			if (v != null)
				b.setFilename(v);

			v = (String) data.get("description");
			if (v != null)
				b.setDescription(v);

			v = (String) data.get("userid");
			if (v != null)
				b.setUserid(v);

			v = (String) data.get("datetaken");
			//Date d = (Date) data.get("datetaken");
			//v= d.toString(); 
			if (v != null)
				b.setDatetaken(v);

			r.add(b);

		}

		return r;

	}

	public void addPhoto(Photo p) {
		try {
			DBCollection col = connect();
			DBObject dob = convertToMongo(p);
			/* AUTO INCREMENTING ID in MONGODB
			 * db.seq.findAndModify({ 
  				query: {"_id": "photo_album"}, 
  				update: {$inc: {"seq":1}}, 
  				new: true 
				}); 
			 * 
			 */
			col.insert(dob);
			SaveImageApp saveImage = new SaveImageApp(); 
			saveImage.savePhotoToMongo(p); 
			//SaveImageApp.savePhotoToMongo(p); 

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


	public List<Photo> getPhoto(String id){

		DBCursor dbc = null;
		List<Photo> list = null;
		try{
			DBCollection col = connect();
			BasicDBObject dob = new BasicDBObject();
			// testing wild card search try... 
			//id = "/.*" + id + ".*/"; 
			dob.append("photoid", id);
			dbc=col.find(dob);
			list=translateFromMongo(dbc);
		}catch(Exception e){
			e.printStackTrace();
		}

		return list;
	}

	public List<Photo> getAllPhotos(){
		DBCursor dbc = null;
		List<Photo> list = null;
		try{
			DBCollection col = connect();
			dbc=col.find();
			list=translateFromMongo(dbc);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * convert our objects to mongo objects
	 * @param b
	 * @return
	 */
	static DBObject convertToMongo(Photo b) {
		BasicDBObject rtn = new BasicDBObject();
		if (b.getPhotoid() != null)
			rtn.append("photoid", b.getPhotoid());
		if (b.getFilename() != null)
			rtn.append("filename", b.getFilename());
		if (b.getUrl() != null)
			rtn.append("url", b.getUrl());    
		if (b.getDescription() != null)
			rtn.append("description", b.getDescription());
		if (b.getUserid() != null)
			rtn.append("userid", b.getUserid());
		if (b.getDatetaken() != null)
			rtn.append("datetaken", b.getDatetaken());
		if (b.getPlacetaken() != null)
			rtn.append("placetaken", b.getPlacetaken());    

		return rtn;
	}
	
	
}
