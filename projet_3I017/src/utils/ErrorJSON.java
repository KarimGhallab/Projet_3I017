package utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe pour générer un message d'erreur au format JSON.
 *
 */
public class ErrorJSON 
{
	/**
	 * Génére un message d'erreur au format JSON
	 * @param message Le message de l'erreur.
	 * @param code Le code d'erreur.
	 * @return Le message d'erreur au format JSON.
	 */
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
