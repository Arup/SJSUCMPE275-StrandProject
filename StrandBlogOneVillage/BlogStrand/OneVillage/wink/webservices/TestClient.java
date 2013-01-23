package OneVillage.wink.webservices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.json.simple.JSONObject;

import javax.ws.rs.core.MediaType;

public class TestClient {

	/**
	 * @param args
	 */
	
	public static String urlString = "http://localhost:8080/onevillage";
	
	public static void main(String[] args) {
		
		createBlog();
//	getblog();
//	getAllblogs();
//		deleteblog();
//		createArticle();
//		deletearticle();
		
	}
	
	public static void createBlog()
	{
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("title", "Blog Entry 6");
		jsonobj.put("description", "This is the test blog entry # 6");
		jsonobj.put("userid", "onevillageTest");
		jsonobj.put("timestamp", getTimestamp());
		 
		String sendObj = jsonobj.toString();
		
		RestClient objClient = new RestClient ();
		String finalurl = urlString + "/blog";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for adding the blog");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(sendObj);
		System.out.println(clresponse.getMessage());
		System.out.println("The create method called was succesfull");
		System.out.println(clresponse.getStatusType());
		
	}
	
	public static void getblog()
	{
				
		RestClient objClient = new RestClient ();
		String finalurl = urlString + "/blog/blog19";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for adding the blog");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		System.out.println(clresponse.getMessage());
		System.out.println("The getBlog method called was succesfull");
		System.out.println(clresponse.getStatusType());
	}
	
	public static void getAllblogs()
	{
				
		RestClient objClient = new RestClient ();
		String finalurl = urlString + "/blog";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for getting all the blogs");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		System.out.println(clresponse.getMessage());
		System.out.println("The getAllBlog method called was succesfull");
	}
	
	public static void deleteblog()
	{
				
		RestClient objClient = new RestClient ();
		String finalurl = urlString + "/blog/0000021/onevillageTest";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for deleting the blog19");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).delete();
		System.out.println(clresponse.getMessage());
		System.out.println("The deleteBlog method called was succesfull");
	}
	
	
	/**
	 * 
	 */
	public static void createArticle()
	{
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("articletitle", "Article - 1");
		jsonobj.put("content", "This is the article added to the blog 19");
		jsonobj.put("userid", "onevillageTest");
		jsonobj.put("timestamp", getTimestamp());
		 
		String sendObj = jsonobj.toString();
		
		RestClient objClient = new RestClient ();
		String finalurl = urlString + "/blog/0000021/article";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for adding the article");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(sendObj);
		System.out.println(clresponse.getMessage());
		System.out.println("Client Id:"+clresponse.getStatusCode());
	}
	
	public static void getAllarticles()
	{
				
		RestClient objClient = new RestClient ();
		String finalurl = urlString + "/blog/blog20/article";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for getting all the articles");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		System.out.println(clresponse.getMessage());
		System.out.println("The getAllArticle method called was succesfull");
	}
	
	public static void getarticle()
	{
				
		RestClient objClient = new RestClient ();
		String finalurl = urlString + "/blog/blog20/article/100000458";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for getting the articles 100000458");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		System.out.println(clresponse.getMessage());
		System.out.println("The getAllArticle method called was succesfull");
	}
	
	public static void deletearticle()
	{
				
		RestClient objClient = new RestClient ();
		String finalurl = urlString +"/blog/0000061/article/100000883/onevillageTest";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for deleting the articles # 100000883");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).delete();
		System.out.println(clresponse.getMessage());
		System.out.println("The getAllArticle method called was succesfull");
	}
	
	public static void updateArticle()
	{
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("title", "Updated Article - 1");
		jsonobj.put("description", "This is the updated article added to the blog");
		jsonobj.put("userid", "onevillageTest");
		jsonobj.put("timestamp", getTimestamp());
		 
		String sendObj = jsonobj.toString();
		
		RestClient objClient = new RestClient ();
		String finalurl = urlString + "/blog/blog20/article/00000772/onevillageTest";
		Resource res = objClient.resource(finalurl);
		System.out.println("Passing the Url to test for updating the article # 00000772");
		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(sendObj);
		
	}
	
	public static String getTimestamp()
	{
		Calendar c =  Calendar.getInstance();
		SimpleDateFormat sdformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		//System.out.println(sdformat.format(c.getTime()));
		String timestamp = sdformat.format(c.getTime());
		return timestamp;
	}
	}


