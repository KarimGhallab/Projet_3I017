package service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;

import base_de_donnees.MessageTools;
import base_de_donnees.UserTools;
import utils.Data;
import utils.ErrorJSON;
import utils.ServiceTools;
/**
 * Classe contenant tous les services en lien avec la gestion de message.
 *
 */
public class MessageServices 
{
	/**
	 * Ajouter un message à la base de données MongoDB.
	 * @param key La clé de connexion.
	 * @param message Le message que l'utilisateur souhaite ajouter.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
	public static JSONObject addMessage(String key, String message)
	{
		if(key == null || message == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{
			if(!base_de_donnees.UserTools.isConnection(key))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
			
			String id = UserTools.getIdUserFromKey(key);
			BasicDBObject storedMessage = MessageTools.addMessage(id , message);
			
			return ServiceTools.serviceAccepted().put("added_message", storedMessage);
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
	
	/**
	 * Supprimer un message à la base de données MongoDB.
 	 * @param key La clé de connexion.
	 * @param id_message Le message à supprimer.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
	public static JSONObject removeMessage(String key, String id_message)
	{
		if(key == null || id_message == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{
			if(!base_de_donnees.UserTools.isConnection(key))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
		
			MessageTools.removeMessage(id_message);
				//return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
			
			return ServiceTools.serviceAccepted().put("removed_message",id_message);
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
	
	/**
	 * Liste les messages d'un utilisateur et d'une liste de ses amis.
	 * @param key La clé de connexion.
	 * @param orderAsc Lister dans un ordre ascendant ou non.
	 * @param limite La limite du nombre de message à lister.
	 * @param amis Les amis pour lequel on veut lister les messages.
	 * @return La liste des messages.
	 */
	public static JSONObject listMessage(String key , String orderAsc, String from, String nbMessage, String amis)
	{
		if(key == null ||from == null || nbMessage == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{	
			boolean order;
			if (orderAsc == null)
				order = false;
			else
				order = Boolean.parseBoolean(orderAsc);
			
			String[] friends;
			if (amis == null)
				friends = null;
			else
				friends = amis.split("-");
			
			
			JSONArray messages;
			
			String userId = base_de_donnees.UserTools.getIdUserFromKey(key); 
			
			if(userId == null)
				messages = MessageTools.listMessage(Integer.parseInt(from), Integer.parseInt(nbMessage), order);
			else
				messages = MessageTools.listMessage(userId, order, Integer.parseInt(from), Integer.parseInt(nbMessage), friends);
				
			if(messages == null)
				return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_DB, Data.CODE_ERROR_DB);
			
			return ServiceTools.serviceAccepted().put("list_message", messages);	// On ajoute la liste des messages
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
}
