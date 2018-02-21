package utils;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe pour générer un message indiquant qu'une opération s'est bien déroulée. Le message est au format JSON.
 *
 */
public class ServiceTools 
{
	/**
	 * Crée un message JSON indiquant le bon déroulement d'une opération.
	 * @return un message au format JSON indiquant qu'une opération s'est bien déroulée.
	 */
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
