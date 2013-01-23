package OneVillagePhoto.repository;



import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import OneVillagePhoto.resource.Photo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
 
/**
 * Java MongoDB : Save image Utility
 * 
 */

public class SaveImageApp {
	
	private static Mongo mongo = null; 
	private static DB db = null; 
	
	public SaveImageApp() {
	try {
		mongo = new Mongo("127.0.0.1", 27017);
		db = mongo.getDB("OneVillage"); 
		
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MongoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//	DB db = mongo.getDB("oneVillage");
//	DBCollection collection = db.getCollection("photo");
	}
	public void savePhotoToMongo(Photo p)
	{
		try {
			 
//			Mongo mongo = new Mongo("127.0.0.1", 27017);
//			DB db = mongo.getDB("oneVillage");
//			DBCollection collection = db.getCollection("photo");

            
			String newFileName = p.getFilename(); 
			System.out.println("************************" );
			System.out.println("URL :: " + p.getUrl() );
			System.out.println("************************" );
			File imageFile = new File(p.getUrl()); 
			// "C:\\Users\\i812558\\Desktop\\star.png"); 
			//("C://Users//i812558//Pictures//From iPhone-2011-07-09//002.JPG"); //  c://hi_cat.jpg");
 
			// create a "photos" namespace  -- to store files and chunks contents
			GridFS gfsPhoto = new GridFS(db, "photos");
 
			// get image file from local drive
			GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
 
			// set a new filename for identify purpose
			gfsFile.setFilename(newFileName);

			// set metadata
			BasicDBObject metadata = new BasicDBObject();
			metadata.put("photoid", p.getPhotoid());
			metadata.put("url", p.getUrl());
			gfsFile.setMetaData(metadata); 
			
			// save the image file into mongoDB
			gfsFile.save();
 
			// print the result
			DBCursor cursor = gfsPhoto.getFileList();
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
 
			System.out.println("Done");
 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
	}
	
	public String getPhotoFromMongo(String filename)
	{
		
		//String newImage = filename + "_new"; 
		GridFS gfsPhoto = new GridFS(db, "photos"); 
		// get image file by it's filename
		GridFSDBFile imageForOutput = gfsPhoto.findOne(filename);
		imageForOutput.getFilename();
		//DBObject iobj = imageForOutput.getMetaData();
		
		// save it into a new image file
		try {
			imageForOutput.writeTo("C:\\Users\\i812558\\Desktop\\newImage.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filename;
		
	}
	
	public void removePhotoFromMongo(String filename)
	{

		//String newImage = filename + "_new"; 
		GridFS gfsPhoto = new GridFS(db, "photos"); 
		
		// remove the image file from mongoDB
		gfsPhoto.remove(gfsPhoto.findOne(filename));		
		System.out.println("Deleted :" + filename); 
		
	}	

}
