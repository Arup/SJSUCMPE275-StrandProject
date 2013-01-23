package OneVillageGroup.webservices;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServiceHelper {

	
	public static Boolean callRemote(String param)
	{
		
		boolean bCallRemote = Boolean.FALSE; 
		
		if (Character.isDigit( param.charAt(0)) ) 
		{ 
			bCallRemote = Boolean.TRUE; 
		} 

		return bCallRemote;
		
	}
	
	// Parse JSON string to get the value of the input ID key
	public static String parseJSON(String json, String idKey) throws ParseException 
	{
		String idValue = null; 
		if (json != null) {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(json);
			JSONObject jobj = (JSONObject) obj;
			idValue =  (String) jobj.get(idKey) ; 
			
			return idValue ; 
		}
		return idValue;
	}	

}
