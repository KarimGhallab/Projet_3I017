package service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			
			String login = UserTools.getLogin(key);
			MessageTools.addMessage(login , message);
			
			return ServiceTools.serviceAccepted().put("added_message", message);
		}
		catch(JSONException e)
		{
			return ErrorJSON.defaultJsonError(Data.MESSAGE_ERROR_JSON, Data.CODE_ERROR_JSON);
		}
	}
	
	/**
	 * Supprimer un message à la base de données MySQL.
 	 * @param key La clé de connexion.
	 * @param message Le message à supprimer.
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
	 * Lister tous les messages d'un utilisateur.
	 * @param key La clé de connexion.
	 * @param userId Le login de l'utilisateur.
	 * @return Un objet JSON indiquant le résultat de l'opération.
	 */
	public static JSONObject listMessage(String key , String userId, String orderAsc, String limite)
	{
		if(key == null || userId == null || orderAsc == null)
			return ErrorJSON.defaultJsonError(Data.MESSAGE_MISSING_PARAMETERS, Data.CODE_MISSING_PARAMETERS);
		try
		{
			if(!base_de_donnees.UserTools.isConnection(key))
				return ErrorJSON.defaultJsonError(Data.MESSAGE_NOT_CONNECTED, Data.CODE_NOT_CONNECTED);
			
			boolean order = Boolean.parseBoolean(orderAsc);
			int limiteEntiere = Integer.parseInt(limite);
			JSONArray messages = MessageTools.listMessage(userId, order, limiteEntiere);
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
