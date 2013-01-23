	package OneVillagePhoto.resource;

public class Photo {

	private String photoid;
	private String userid;
	private String datetaken;
	private String placetaken;
	private String description;
	private String filename;
	private String url;  // this is qualified file name with path where host is represented as IP address
						 // for e.g. "\\127.0.0.1\c$\Users\i812558\Desktop\star.png"

	public String getPhotoid() {
		return photoid;
	}

	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getDatetaken() {
		return datetaken;
	}

	public void setDatetaken(String datetaken) {
		this.datetaken = datetaken;
	}

	public String getPlacetaken() {
		return placetaken;
	}

	public void setPlacetaken(String placetaken) {
		this.placetaken = placetaken;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	
}
