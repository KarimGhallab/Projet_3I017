package utils;


import org.json.JSONException;
import org.json.JSONObject;

public class ServiceTools 
{
	public static JSONObject serviceAccepted()
	{
		try
		{
			JSONObject reponse = new JSONObject ();
			reponse.put("status","ok");
			return reponse;
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError("error JSON", 101);
		}
		
	}
}
