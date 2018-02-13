package service;

import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import base_de_donnees.ConnexionBd;
import base_de_donnees.MessageTools;
import base_de_donnees.UserTools;
import utils.Data;
import utils.ErrorJSON;
import utils.ServiceTools;

public class MessageServices 
{
	public static JSONObject addMessage(String key , String message)
	{
		if(key == null || message == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{
			if(!ConnexionBd.isConnection(key))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
			
			String login = UserTools.getLogin(key);
			if(! MessageTools.addMessage(login , message))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
			
			return ServiceTools.serviceAccepted().put("message_added", message);
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
	
	
	public static JSONObject removeMessage(String key , String message)
	{
		if(key == null || message == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{
			if(!ConnexionBd.isConnection(key))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
			
			String login = UserTools.getLogin(key);
			if(! MessageTools.removeMessage(login , message))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
			
			return ServiceTools.serviceAccepted().put("message_removed", message);
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
}
