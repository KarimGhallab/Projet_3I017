package base_de_donnees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
/**
 * 
 * Classe contenant les méthodes statiques pour gérer les messages avec notre base de données MongDB.
 */
public class MessageTools
{
	/**
	 * Ajouter un message à la base de données.
	 * @param login Le login utilisateur qui souhaite ajouter un message.
	 * @param message Le message à ajouter.
	 * @return True si le message à été ajouté avec succès. False sinon.
	 */
	public static void addMessage(String userID , String message)
	{
		DBCollection msg = DataBase.getMongoCollection("Message");
		BasicDBObject query = new BasicDBObject();
		GregorianCalendar c= new GregorianCalendar();
		query.put("user_id", userID);
		query.put("content", message);
		query.put("date", c.getTime());
		query.put("comments", Collections.EMPTY_LIST);
		msg.insert(query);
	}

	/**
	 * Supprimer un message de la base de données.
	 * @param login Le login utilisateur qui souhaite supprimer le message.
	 * @param message Le message à supprimer.
	 * @return True si le message à été supprimé avec succès. False sinon.
	 */
	public static void removeMessage(String id_message) 
	{
		DBCollection msg = DataBase.getMongoCollection("Message");
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id_message));
		System.out.println("Query : " +query);
		msg.remove(query);
	}
	
	/**
	 * Renvoyer la liste des messages d'un utilisateur
	 * @param login Le login pour lequel il faut chercher la liste des messages.
	 * @param orderAsc Indique si on effectue un trie des messages par date croissante.
	 * @param limite Le nombre de messag à afficher. Si la limte est inférieur ou égal à 0, on affiche tous les messages.
	 * @return La liste des messages de l'utilisateur. Ou null en cas d'erreur.
	 */
	public static JSONArray listMessage(String id_user, boolean orderAsc, int limite, String[] id_friends) 
	{
		DBCollection msg = DataBase.getMongoCollection("Message");
		BasicDBObject query = new BasicDBObject();
		if(id_friends == null)
			query.put("user_id", id_user);
		else
			query.put("user_id", new BasicDBObject("$in", id_friends));
		DBCursor messagesCursor = msg.find(query);
		if (!orderAsc)
			messagesCursor.sort(new BasicDBObject("date", -1));
		else
			messagesCursor.sort(new BasicDBObject("date", 1));
		if (limite > 0)
			messagesCursor.limit(limite);
		JSONArray userMessages = new JSONArray();
		try
		{
			while(messagesCursor.hasNext())
			{
				JSONObject json = new JSONObject();
				BasicDBObject auteur = new BasicDBObject();
				DBObject document = messagesCursor.next();
				auteur.put("login", UserTools.getLoginFromId(Integer.parseInt(id_user)));
				auteur.put("user_id", document.get("user_id"));
				json.put("content", document.get("c_ontent"));
				json.put("auteur", auteur);
				json.put("date", document.get("date"));
				json.put("id", document.get("_id"));
				userMessages.put(json);
			}
			return userMessages;
		}
		catch(Exception e)
		{
			System.err.println("listMessage : " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Liste l'ensemble des messages de l'application.
	 * @param limite Le nombre de message à afficher.
	 * @return La liste des messages.
	 */
	public static JSONArray listMessage(int limite) 
	{
		DBCollection msg = DataBase.getMongoCollection("Message");
		DBCursor messagesCursor = msg.find();
		if (limite > 0)
			messagesCursor.limit(limite);
		JSONArray userMessages = new JSONArray();
		try
		{
			while(messagesCursor.hasNext())
			{
				JSONObject json = new JSONObject();
				DBObject document = messagesCursor.next();
				json.put("content", document.get("content"));
				json.put("date", document.get("date"));
				json.put("id", document.get("_id"));
				json.put("user_id", document.get("user_id"));
				userMessages.put(json);
			}
			return userMessages;
		}
		catch(Exception e)
		{
			System.err.println("listMessage : " + e.getMessage());
			return null;
		}
	}
}
