package service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import base_de_donnees.UserTools;
import utils.Data;
import utils.ErrorJSON;
import utils.ServiceTools;

/**
 * Classe contenant tous les services en lien avec la gestion d'ami.
 *
 */
public class FriendServices 
{
	/**
	 * Ajouter un ami à la base de données MySQL.
	 * @param idFriend L'id de l'ami.
	 * @param key La clé de connexion.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
	public static JSONObject addFriend(String idFriend , String key)
	{
		if(key == null || idFriend == null )
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		
		if(!base_de_donnees.UserTools.isConnection(key))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		
		String idUser = base_de_donnees.UserTools.getIdUserFromKey(key); 
		
		if(!UserTools.userExistsId (Integer.parseInt(idUser)))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_USER_DOES_NOT_EXIST, Data.CODE_USER_DOES_NOT_EXIST);
		
		if(!UserTools.userExistsId (Integer.parseInt(idFriend)))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_FRIEND_DOES_NOT_EXIST, Data.CODE_FRIEND_DOES_NOT_EXIST);
		
		if(!UserTools.addFriend(idUser, idFriend))		// Ajout de la Friendship
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
		
		return ServiceTools.serviceAccepted();
	}
	
	/**
	 * Supprimer un ami à la base de données MySQL.
	 * @param idFriend L'id de l'ami.
	 * @param key La clé de connexion.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
	public static JSONObject removeFriend(String idFriend , String key)
	{
		if(key == null || idFriend == null )
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
	
		if(!base_de_donnees.UserTools.isConnection(key))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		String idUser = base_de_donnees.UserTools.getIdUserFromKey(key); 
		if(!UserTools.userExistsId (Integer.parseInt(idUser)))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_USER_DOES_NOT_EXIST, Data.CODE_USER_DOES_NOT_EXIST);
		if(!UserTools.userExistsId (Integer.parseInt(idFriend)))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_FRIEND_DOES_NOT_EXIST, Data.CODE_FRIEND_DOES_NOT_EXIST);	
		
		if(!UserTools.removeFriend(idUser, idFriend))		//Supression de la Friendship
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
		
		return ServiceTools.serviceAccepted();
	}
	
	/**
	 * Lister tous les amis d'un utilisateur.
	 * @param key La clé de connexion.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
	public static JSONObject listFriend(String key)
	{
		if(key == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		if(!base_de_donnees.UserTools.isConnection(key))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		
		String idUser = base_de_donnees.UserTools.getIdUserFromKey(key); 
		List<Integer> liste = UserTools.listFriend(idUser);
		if(liste == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
		
		try
		{
			return ServiceTools.serviceAccepted().put("list_friend", liste);
		}
		catch (JSONException e)
		{
			System.err.println("Error listFriend : " + e.getMessage());
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
}
