package service;

import java.util.ArrayList;

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
	 * @param idUser L'id de l'utilisateur qui souhaite ajouter un ami.
	 * @param idFriend L'id de l'ami.
	 * @param key La clé de connexion.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
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
	
	/**
	 * Supprimer un ami à la base de données MySQL.
	 * @param idUser L'id de l'utilisateur qui souhaite supprimer un ami.
	 * @param idFriend L'id de l'ami.
	 * @param key La clé de connexion.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
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
	
	//////////////////////////////À verifier
	/**
	 * Lister tous les amis d'un utilisateur.
	 * @param idUser L'id de l'utilisateur.
	 * @param key La clé de connexion.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
	public static JSONObject listFriend(String idUser, String key)
	{
		if(idUser == null || key == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		if(!base_de_donnees.UserTools.isConnection(key))
			return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		ArrayList<String> liste=null;
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
