package OneVillagePhoto.webservices;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import OneVillagePhoto.repository.PhotoAlbumClient;
import OneVillagePhoto.repository.PhotoClient;
import OneVillagePhoto.resource.Album;
import OneVillagePhoto.resource.Photo;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;

@Path("/photo")
// @javax.ws.rs.ext.Provider
@SuppressWarnings({"unchecked", "unused", "rawtypes"} )
public class PhotoServicesImpl {

	public static PhotoServicesImpl service;

	private static Logger logger = Logger.getLogger("PhotoServicesImpl.class");

	public synchronized static PhotoServicesImpl getInstance() {
		if (service == null) {
			service = new PhotoServicesImpl();
		}

		return service;
	}

	// Testing .....
	// Related Path : http://localhost:8080/OneVillagePhoto/photo/about
	@Path("/about")
	@GET
	public String about() {

		return "Hello  from Photo Application.... ";
	}

	// CREATE ALBUM
	// Related Path : http://localhost:8080/OneVillagePhoto/photo/album
	@Path("/album")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAlbum(String JSONObject) {
		
		if (JSONObject==null)
			return Response.status(400).entity("Bad Request - Enter proper Album details").build();
		
		Album album = null;
		try {
			System.out.println(JSONObject);
			System.out.println("Converting JSON to Album type");
			Gson gsonObject = new Gson();
			album = gsonObject.fromJson(JSONObject, Album.class);
			// String albumid = generateID();
			// album.setAlbumid(albumid);

		} catch (Exception e) {
			return Response.status(400).build();
		}

		PhotoAlbumClient albumclient = new PhotoAlbumClient();
		System.out.println("Adding the Album to database");
		albumclient.addAlbum(album);
		System.out.println(" !!~ Succesfully Added ~!!");

		return Response.status(200).entity("Album created").build();
	}

	// GET ALL ALBUMS
	// Related Path : http://localhost:8080/oneVillage/photo/album
	@Path("/album")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllAlbums() {
		PhotoAlbumClient albumclient = new PhotoAlbumClient();

		System.out.println("Getting list of all Albums");
		List<Album> list = albumclient.getAllAlbums();

//		Type srcType = new TypeToken<List<Album>>() {
//		}.getType();
//
//		System.out.println("Converting to Json");
//		Gson gsonObject = new Gson();
//		String albumList = gsonObject.toJson(list, srcType);
		
		JSONArray albumList = new JSONArray();
		
		Iterator i = list.iterator(); 
		
		while (i.hasNext()) {
			Album g= (Album)i.next(); 
			JSONObject albumJson = new JSONObject();
	
			albumJson.put("albumid", g.getAlbumid()); 
			albumJson.put("title", g.getTitle());
			albumJson.put("description", g.getDescription());
			albumJson.put("userid", g.getUserid());
			albumList.add(albumJson);

		}		
		
		System.out.println("Returning json Object ");
		return albumList.toString();		

	}

	// ADD A PHOTO IN AN ALBUM
	// Related Path : http: //localhost:8080/oneVillage/photo/album/{albumid}
	@Path("/album/{albumid}")
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPhoto(@PathParam("albumid") String albumid, String JSONObject) {
		// for testing use hard-coded album

		Photo photo = null;
		try {
			System.out.println(JSONObject);
			Gson gsonObject = new Gson();
			photo = gsonObject.fromJson(JSONObject, Photo.class);
			// photo.setAlbumid(albumid);
			// photo.setPhotoid(generateID());

		} catch (Exception e) {
			return Response.status(400).build();
		}

		
		System.out.println("Calling method for saving Image in Mongo DB");
		PhotoClient photoclient = new PhotoClient();
		photoclient.addPhoto(photo);

		System.out.println("Calling method for adding Photo to Album entry client");
		PhotoAlbumClient aclient = new PhotoAlbumClient(); 
		aclient.addPhotoToAlbum(albumid, photo); 		

		return Response.status(200).entity("Album entry created").build();
	}

	// GET ALL PHOTOS OF AN ALBUM
	// Related Path : http:: //localhost:8080/oneVillage/photo/album/{albumid}
	@Path("/album/{albumid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllPhotos(@PathParam("albumid") String albumid) {
		PhotoAlbumClient albumclient = new PhotoAlbumClient();

		System.out.println("Getting list of all Albums");
		List<Photo> photoList = albumclient.viewAllPhotos(albumid);

		System.out.println("Converting to JSON ");

		JSONArray photos = new JSONArray();
		
		if (photoList != null)
		{
			Iterator i = photoList.iterator(); 
			
			while (i.hasNext()) {
				BasicDBObject data = (BasicDBObject) i.next();  

				JSONObject photoJson = new JSONObject();
				
				if (data.get("photoid")!= null)
					photoJson.put("photoid", data.get("photoid")); 
				if (data.get("description")!= null)
					photoJson.put("description", data.get("description"));
				if (data.get("userid")!= null)
					photoJson.put("userid", data.get("userid"));
				if (data.get("filename")!= null)
					photoJson.put("filename", data.get("filename"));
				String url = data.get("url").toString() ;
				//url = url.replace("/", "\\/"); 
				if (data.get("url")!= null)
					photoJson.put("url", url);
				
				System.out.println("URL = ");
				System.out.print(url);
				
				photos.add(photoJson);
	
			}		
		}
		else 
		{
			return "No photos found"; 
		}
		System.out.println("Returning json Object ");
		System.out.println(photos);
		return photos.toString();

	}

	
	 
	  //GET A PHOTO FROM AN ALBUM //Related Path : http::
	  //localhost:8080/oneVillage/photo/album/{albumid}/{photoid}
	  
	  @Path("/album/{albumid}/{photoid}")
	  @GET
	  @Produces(MediaType.APPLICATION_JSON) 
	  public String getPhoto(@PathParam("albumid") String albumid, @PathParam("photoid") String photoid) {

		  if (photoid == null)
				return Response.status(400).entity("Bad Request-Enter proper PhotoID").build().toString(); 
				
			String rtnPhoto = null;  
			System.out.println("Fetching Photo : " + photoid); 
			

				
				PhotoClient photoClient = new PhotoClient();
				List<Photo> list = photoClient.getPhoto(photoid);

				Iterator i = list.iterator(); 
				JSONObject photoJson = new JSONObject();
				while (i.hasNext()) {
					OneVillagePhoto.resource.Photo g= (OneVillagePhoto.resource.Photo)i.next();
					
					if (g.getPhotoid()!=null)
						photoJson.put("photoid", g.getPhotoid()); 
					if (g.getFilename()!=null)
						photoJson.put("filename", g.getFilename());
					if (g.getDescription()!=null)
						photoJson.put("description", g.getDescription());
					if (g.getUserid()!=null)
						photoJson.put("userid", g.getUserid());
					if (g.getUrl()!=null)
						photoJson.put("url", g.getUrl());

					System.out.println("Returning json Object ");
					rtnPhoto = photoJson.toString();
					return rtnPhoto; 
				}		
				
				return rtnPhoto; 
		  
	  }
	  
	 /* 
	 * //DELETE A PHOTO IN AN ALBUM //Related Path : http:
	 * //localhost:8080/oneVillage/photo/{albumid}/{userid}/{photoid}
	 * 
	 * @Path("/{albumid}/{userid}/{photoid}")
	 * 
	 * @DELETE public Response deletePhoto(String albumid, String userid, String
	 * photoid) {
	 * 
	 * return null; }
	 * 
	 * 
	 * @Override public Response createphoto(String JSONObject) {
	 * 
	 * Photo photo = null; try { System.out.println(JSONObject);
	 * System.out.println("Converting JSON to Photo type"); Gson gsonObject =
	 * new Gson(); photo = gsonObject.fromJson(JSONObject, Photo.class); String
	 * photoid = generateID(); photo.setPhotoid(photoid);
	 * 
	 * } catch(Exception e){ return Response.status(400).build(); }
	 * 
	 * PhotoClient photoclient = new PhotoClient();
	 * System.out.println("Adding the Photo to database");
	 * photoclient.addPhoto(photo);
	 * System.out.println(" !!~ Succesfully Added ~!!");
	 * 
	 * return Response.status(200).entity("Photo created").build();
	 * 
	 * }
	 * 
	 * @Override public String getAllPhotos() {
	 * 
	 * 
	 * PhotoClient photoclient = new PhotoClient();
	 * 
	 * System.out.println("Getting list of all Photos"); List<Photo> list=
	 * photoclient.getAllPhotos();
	 * 
	 * Type srcType = new TypeToken<List<Photo>>(){}.getType();
	 * 
	 * System.out.println("Converting to Json"); Gson gsonObject = new Gson();
	 * String photoList = gsonObject.toJson(list, srcType);
	 * 
	 * System.out.println("Returning json Object "); return photoList;
	 * 
	 * }
	 * 
	 * @Override public String getphoto(String photoid) {
	 * 
	 * 
	 * PhotoClient photoclient = new PhotoClient(); List<Photo> list =
	 * photoclient.getPhoto(photoid); Type srcType = new
	 * TypeToken<List<Photo>>(){}.getType();
	 * 
	 * Gson gsonObject = new Gson(); String rtnPhoto = gsonObject.toJson(list,
	 * srcType);
	 * 
	 * return rtnPhoto;
	 * 
	 * 
	 * }
	 * 
	 * @Override public Response deletephoto(String photoid, String userid) {
	 * 
	 * 
	 * PhotoClient photoclient = new PhotoClient(); PhotoArticle photoarticle =
	 * new PhotoArticle();
	 * 
	 * System.out.println("Removing the photo from database");
	 * photoclient.removePhoto(photoid, userid); photoarticle.remove(photoid);
	 * 
	 * return Response.status(200).entity("Photos Deleted").build();
	 * 
	 * }
	 * 
	 * public static synchronized String generateID() {return
	 * String.valueOf(UUID.randomUUID());
	 * 
	 * }
	 */

	// testing by RS

	public static void main(String args[]) {

		// PhotoClient photoclient = new PhotoClient();
		PhotoServicesImpl photoServer = PhotoServicesImpl.getInstance();

		// List<Photo> list = photoclient.getPhoto("photo1");
		// Type srcType = new TypeToken<List<Photo>>(){}.getType();

		// Gson gsonObject = new Gson();
		// String rtnPhoto = gsonObject.toJson(list, srcType);

		// create album 
//		String newPhotoAlbum = "{ albumid: \"1\", userid: \"user1\", title: \"The Truth About Cars\", description: \"The Truth About Cars is an automotive photo with teeth.\", timestamp: \"2011-01-20\"}";
//		Response rs = photoServer.createAlbum(newPhotoAlbum);
//		System.out.println("---> Your Photo ::: " + rs);

//		// view all photos
//		String photos = photoServer.getAllPhotos("1"); 
//		System.out.println("---> Your Photo ::: " + photos);

		// create photo
//		 String photo =
//		 "{ photoid: \"2\", userid: \"Sulagna\", filename: \"ellipse.jpg\", url: \"C:/Users/i812558/Desktop/ellipse.jpg\", description: \"Cute Little Ellipse in MSPaint\", timetaken: \"2011-01-20\"}";
//		 Response rs = photoServer.addPhoto("1", photo);
//		 System.out.println("---> Your Photo ::: " + rs);
		 
		// get photo
		 String photo = photoServer.getPhoto("1", "2");
			 System.out.println("---> Your Photo ::: " + photo); 

	}

}
