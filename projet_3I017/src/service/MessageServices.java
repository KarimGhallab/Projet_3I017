package service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import base_de_donnees.MessageTools;
import base_de_donnees.UserTools;
import utils.Data;
import utils.ErrorJSON;
import utils.ServiceTools;

public class MessageServices 
{
	public static JSONObject addMessage(String key, String message)
	{
		if(key == null || message == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{
			if(!base_de_donnees.UserTools.isConnection(key))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
			
			String login = UserTools.getLogin(key);
			if(! MessageTools.addMessage(login , message))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
			
			return ServiceTools.serviceAccepted().put("added_message", message);
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
	
	
	public static JSONObject removeMessage(String key, String message)
	{
		if(key == null || message == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{
			if(!base_de_donnees.UserTools.isConnection(key))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
			
			String login = UserTools.getLogin(key);
			if(! MessageTools.removeMessage(login , message))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
			
			return ServiceTools.serviceAccepted().put("removed_message", message);
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
	
	public static JSONObject listMessage(String key , String login)
	{
		if(key == null || login == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{
			if(!base_de_donnees.UserTools.isConnection(key))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
			
			ArrayList<String> messages = MessageTools.listMessage(login);
			if(messages == null)
				return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
			
			return ServiceTools.serviceAccepted().put("list_message", messages.toString());
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
}
