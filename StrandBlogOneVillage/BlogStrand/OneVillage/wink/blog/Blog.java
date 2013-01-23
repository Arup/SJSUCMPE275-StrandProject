package OneVillage.wink.blog;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class Blog {
	
	private String blogid;
	private String userid;
	private String title;
	private String description;
	private String timestamp;
	private List<Article> articles;
	
	
	public String getBlogid() {
		return blogid;
	}
	public void setBlogid(String blogid) {
		this.blogid = blogid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public List<Article> getData() {
		return articles;
	}

	public void setData(List<Article> data) {
		this.articles = data;
	}

	public void addData(Article B) {
		if (B != null)
			articles.add(B);
	}
}
