package OneVillage.wink.blog;

import java.util.ArrayList;
import java.util.List;


public class ArticleList {
	
	private ArrayList<Article> data = new ArrayList<Article>();

	public ArrayList<Article> getData() {
		return data;
	}

	public void setData(ArrayList<Article> data) {
		this.data = data;
	}

	public void addData(Article B) {
		if (B != null)
			data.add(B);
	}

}
