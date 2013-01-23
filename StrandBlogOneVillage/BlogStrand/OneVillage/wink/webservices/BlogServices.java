package OneVillage.wink.webservices;


import javax.ws.rs.Path;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSONObject;


@Path("/blog")
public interface BlogServices {

	
	// Related Path : http://localhost:8080/OneVillage/blog
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response createblog(String JSONObject);


	// Related Path : http://localhost:8080/oneVillage/blog/
@GET
@Produces(MediaType.APPLICATION_JSON)
public String getAllBlogs ();


	// Related Path : http://localhost:8080/OneVillage/blog /{blogid}
@Path("/{blogid}")
@GET 
@Produces(MediaType.APPLICATION_JSON)
public String getblog(@PathParam("blogid") String blogid);


	//Related Path : http: //localhost:8080/oneVillage/blog/{blogid}/{userid}
@Path("/{blogid}/{userid}")
@DELETE
public Response deleteblog(@PathParam("blogid") String blogid,@PathParam("userid") String userid);


	//Related Path : http: //localhost:8080/oneVillage/blog/{blogid}/article
@Path("/{blogid}/article")
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response createarticle(@PathParam("blogid") String blogid, String JSONObject);


	//Related Path : http:: //localhost:8080/oneVillage/blog/{blogid}/article
@Path("/{blogid}/article")
@GET
@Produces(MediaType.APPLICATION_JSON)
public String getAllArticles (@PathParam("blogid") String blogid);


	// Related Path : http://localhost:8080/oneVillage/blog/{blogid}/article/{articleid}
@Path("/{blogid}/article/{articleid}")
@GET
@Produces(MediaType.APPLICATION_JSON)
public String getarticle(@PathParam("blogid") String blogid, @PathParam("articleid") String articleid);


	//Related Path : http: //localhost:8080/oneVillage/blog/{blogid}/article/{articleid}/{userid}
@Path("/{blogid}/article/{articleid}/{userid}")
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response deletearticle(@PathParam("blogid") String blogid,@PathParam("articleid") String articleid,@PathParam("userid") String userid);


	//Related Path : http://localhost:8080/OneVillage/blog/{blogid}/article/{articleid}/{userid}
@Path("/{blogid}/article/{articleid}/{userid}")
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response updatearticle(@PathParam("blogid") String blogid,@PathParam("articleid") String articleid,@PathParam("userid") String userid, String JSONObject);








	
	

}
