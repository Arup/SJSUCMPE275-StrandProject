package OneVillage.wink.webservices;

import OneVillage.wink.blog.database.BlogArticle;
import OneVillage.wink.blog.database.BlogClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Calendar;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.json.simple.JSONObject;
//import org.json.JSONObject;

import OneVillage.wink.blog.Article;
import OneVillage.wink.blog.Blog;



public class BlogServicesImplementation implements BlogServices {

	public static BlogServicesImplementation service; 
 
	
	public synchronized static BlogServicesImplementation getInstance() {
		if (service == null) {
			service = new BlogServicesImplementation();
		}

		return service;
	}
	
	@Override
	public  Response createblog(String JSONObject) {
		
		Blog blog = null;
		try {
			
			System.out.println("Converting the object to JAVA Blog type using Gson");
			//JSONObject object = new JSONObject();
			
						 
			Gson gsonObject = new Gson();	
			blog = gsonObject.fromJson(JSONObject, Blog.class); 
			System.out.println("created using gson");
			
			Random rand = new Random(); 
			  int random = rand.nextInt(100);
			String blogid  = "00000" + Integer.toString(random);
			blog.setBlogid(blogid);
			
		} catch(Exception e){
			return Response.status(400).build();
		}
			BlogClient blogclient = new BlogClient();
			System.out.println("Adding the Blog to database");
			blogclient.addBlog(blog);
			System.out.println("!!~ Succesfully Added ~!!");
			
			return Response.status(200).entity("New Blog").build();
		
		
	}

	@Override
	public String getAllBlogs() {
		
		
		BlogClient blogclient = new BlogClient();
		
		System.out.println("Geeting list of all Blogs");
		List<Blog> list= blogclient.getAllBlogs();
		
		Type srcType = new TypeToken<List<Blog>>(){}.getType();
		
		System.out.println("Json Object conversion");
		Gson gsonObject = new Gson();
		String blogList = gsonObject.toJson(list, srcType);
		
		System.out.println("Return json Object ");
		return blogList;
		
	}

	@Override
	public String getblog(String blogid) {
		

		BlogClient blogclient = new BlogClient();
		List<Blog> list = blogclient.getBlog(blogid);
		Type srcType = new TypeToken<List<Blog>>(){}.getType();
		
		Gson gsonObject = new Gson();
		String rtnBlog = gsonObject.toJson(list, srcType);

		return rtnBlog;

		
	}

	@Override
	public Response deleteblog(String blogid, String userid) {
		
		
		BlogClient blogclient = new BlogClient();
		BlogArticle blogarticle = new BlogArticle();
		
		System.out.println("Removing the blog from database");
		blogclient.removeBlog(blogid, userid);
		blogarticle.remove(blogid, userid);
		
		return Response.status(200).entity("Blogs Deleted").build();
	
	}

	@Override
	public Response createarticle(String blogid, String JSONObject) {
		
		Article article = null;
		try{
			Random rand = new Random(); 
			  int random = rand.nextInt(1000);
			String articleid  = "100000" + Integer.toString(random);
			System.out.println(JSONObject);
			Gson gsonObject = new Gson();
			article = gsonObject.fromJson(JSONObject, Article.class);
			article.setBlogid(blogid);
			article.setArticleid(articleid);
						
			}catch(Exception e){
				return Response.status(400).build();
			}
		
		System.out.println("Adding the article");
		BlogArticle blogarticle = new BlogArticle();
		blogarticle.addArticle(blogid, article);
		
		return Response.status(200).entity("Article created").build();
		
		
	}

	@Override
	public String getAllArticles(String blogid) {
		
		BlogArticle blogarticle = new BlogArticle();
		
		System.out.println("Getting the List of all articles");
		List<Article> articlelist = blogarticle.getAllArticles(blogid);
		
		System.out.println("Checking the type of List");
		Type srcType = new TypeToken<List<Article>>(){}.getType();
	
		System.out.println("Converting to JSON using gson");
		Gson gsonObject = new Gson();
		String rtnArticlestr = gsonObject.toJson(articlelist, srcType);
		
		System.out.println("Returning the json string");
		return rtnArticlestr;
		
	}

	@Override
	public String getarticle(String blogid, String articleid) {
		
		BlogArticle article = new BlogArticle();
		List<Article> articlelist = article.getArticle(blogid, articleid);
		Type srcType = new TypeToken<List<Article>>(){}.getType();
		
		Gson gsonObject = new Gson();
		String rtnArticle = gsonObject.toJson(articlelist, srcType);

		return rtnArticle;
		
	}

	@Override
	public Response deletearticle(String blogid, String articleid, String userid) 
	{
		
		System.out.println("Removing the Article");
		BlogArticle blogarticle = new BlogArticle();
		blogarticle.removeArticle(blogid, articleid, userid);	
		return Response.status(200).entity("Blog Entries deleted succesfully").build();
		
		
	}

	@Override
	public Response updatearticle(String blogid, String articleid, String userid, String JSONObject) {
		
		BlogArticle blogarticle = new BlogArticle();
		Article article = new Article();
		System.out.println(JSONObject);
		System.out.println("Converting JSON to Article type");
		Gson gsonObject = new Gson();
		article = gsonObject.fromJson(JSONObject, Article.class);
		
		blogarticle.updateArticle(blogid, articleid, userid, article);
		return Response.status(200).entity("Article Updated Successfully ").build();
				
	}
	
	public static synchronized String generateID()
	{
		return String.valueOf(UUID.randomUUID());
			}
	
	public static void main(String[] args) {
		
		BlogServicesImplementation blogServer = BlogServicesImplementation.getInstance();
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("articletitle", "Sample Article - 1");
		jsonobj.put("content", "This is the article added to the blog");
		jsonobj.put("userid", "onevillageTest");
		jsonobj.put("timestamp", "12-23-2001");
		 
		String sendObj = jsonobj.toString();
		String blogid = "blog22";
		Response articleresponse = blogServer.deletearticle("0000061", "100000883", "onevillageTest");
				System.out.println(articleresponse);

//		RestClient objClient = new RestClient ();
//		String finalurl = urlString + "/blog/blog20/article";
//		Resource res = objClient.resource(finalurl);
//		System.out.println("Passing the Url to test for adding the article");
//		ClientResponse clresponse = res.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(sendObj);
//		System.out.println(clresponse.getMessage());

		
//		JSONObject JSONObject = new JSONObject();
//		JSONObject.put("blogid","blog22");
//		JSONObject.put("title","this my new blog");
//		JSONObject.put("description"," need to be added");
//		JSONObject.put("userid","zan");
//		JSONObject.put("timestamp", "2009-10-12");
//		//JSONObject.put("articles", " ");
//		
//		System.out.println(JSONObject.toString());
//		String blog = JSONObject.toString();
		
		//String response = blogServer.getblog("blog4");
		
	
//	Response response1 = blogServer.createblog(blog);
//		System.out.println("exiting from blog -  blog created");
//		System.out.println(response1);
////		
//System.out.print("Adding an article");
////		
//		JSONObject sendobject = new JSONObject();
//		//sendobject.put("articleid", "article3");
//		sendobject.put("articletitle", "first Article testing");
//		sendobject.put("content","Test blog entry from a client");
//		sendobject.put("tags","user1");
//		sendobject.put("timestamp", "2009-10-12"); 
////		
////		
////					
//		String send=sendobject.toString();
//		System.out.println("Sending object"+send);
//		String blogid = "blog20";
//		Response articleresponse = blogServer.createarticle(blogid, send);
//////
//	//Response articleresponse = blogServer.deleteblog("blog19", "zan");
//////		System.out.println("Sending object");
////		
////		String Allblogs = blogServer.getAllBlogs();
////		System.out.println(Allblogs);
////		
//		String blog1 = blogServer.getAllBlogs();
//		System.out.println(blog1);
//		
	}


}
