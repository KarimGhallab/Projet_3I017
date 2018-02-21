package service;

import org.json.JSONException;
import org.json.JSONObject;
import base_de_donnees.UserTools;
import utils.Data;
import utils.ErrorJSON;
import utils.ServiceTools;

public class UserServices 
{
	public static JSONObject login(String login, String pwd, String root) 
	{
		if(login==null || pwd == null || root == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		int myRoot = Integer.parseInt(root);
		if ((myRoot != 0) && (myRoot != 1))
			myRoot = 0;
		
		if(!base_de_donnees.UserTools.userExists(login))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_USER_DOES_NOT_EXIST, Data.CODE_USER_DOES_NOT_EXIST);
		if(!base_de_donnees.UserTools.checkPwd(login,pwd))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_INCORRECT_LOGIN_PASSWORD, Data.CODE_INCORRECT_LOGIN_PASSWORD);
		String key = base_de_donnees.UserTools.insererConnexion(login, pwd, myRoot);
		if (key == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
		else
		{
			try
			{
				return ServiceTools.serviceAccepted().put("key", key);
			}
			catch (JSONException e) 
			{
				return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
			}
		}	
	}
	
	public static JSONObject logout(String key)
	{
		if(key == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		if(!UserTools.isConnection(key))		// Utilisateur non connecté
			return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		
		if(UserTools.removeConnection(key))		//On enleve la connextion
			return ServiceTools.serviceAccepted();
		else
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
	}
	
	public static JSONObject createUser (String login , String pwd , String prenom , String nom , String email)
	{
		if(login==null || pwd == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		if(base_de_donnees.UserTools.userExists(login))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_USER_ALREADY_EXISTS, Data.CODE_USER_ALREADY_EXISTS);
		
		boolean res = base_de_donnees.UserTools.insererUser(login , pwd , prenom , nom , email);
		if (res)
			return ServiceTools.serviceAccepted();
		else
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
	}
}
