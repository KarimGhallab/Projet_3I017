package utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorJSON 
{
	public static JSONObject defaultJsonError (String message, int code)
	{
		JSONObject erreur = new JSONObject();
		try 
		{
			erreur.put("status", "ko");
			erreur.put("message", message);
			erreur.put("code", code);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return erreur;
	}
}
