package service;

import org.json.JSONException;
import org.json.JSONObject;
import base_de_donnees.UserTools;
import utils.Data;
import utils.ErrorJSON;
import utils.ServiceTools;

public class FriendServices 
{
	
	public static JSONObject addFriend(String idUser , String idFriend , String key)
	{
		if(idUser == null || idFriend == null )
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
			
		if(!UserTools.userExists (Integer.parseInt(idUser)))
		{
			System.out.println("idUser : " + idUser);
			return ErrorJSON.defaultJsonError(Data.MESSAGE_USER_DOES_NOT_EXIST, Data.CODE_USER_DOES_NOT_EXIST);
		}
		if(!UserTools.userExists (Integer.parseInt(idFriend)))
		{
			System.out.println("idFriend : " + idFriend);
			return ErrorJSON.defaultJsonError(Data.MESSAGE_FRIEND_DOES_NOT_EXIST, Data.CODE_FRIEND_DOES_NOT_EXIST);
		}
		
		if(!base_de_donnees.UserTools.isConnection(key))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		
		if(!UserTools.addFriend(idUser, idFriend))		// Ajout de la Friendship
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
		
		return ServiceTools.serviceAccepted();
	}
	
	public static JSONObject removeFriend(String idUser , String idFriend , String key)
	{
		if(idUser == null || idFriend == null )
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
			
		if(!UserTools.userExists (idUser))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_USER_DOES_NOT_EXIST, Data.CODE_USER_DOES_NOT_EXIST);
		if(!UserTools.userExists (idFriend))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_FRIEND_DOES_NOT_EXIST, Data.CODE_FRIEND_DOES_NOT_EXIST);
		
		if(!base_de_donnees.UserTools.isConnection(key))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		
		if(!UserTools.removeFriend(idUser, idFriend))		//Supression de la Friendship
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
		
		return ServiceTools.serviceAccepted();
	}
	
	public static JSONObject listFriend(String idUser, String key)
	{
		if(idUser == null || key == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		if(!base_de_donnees.UserTools.isConnection(key))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		String liste;
		if((liste=UserTools.listFriend(idUser)) == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
		
		try
		{
			return ServiceTools.serviceAccepted().put("list", liste);
		}
		catch (JSONException e)
		{
			System.err.println("Error listFriend : " + e.getMessage());
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
}
