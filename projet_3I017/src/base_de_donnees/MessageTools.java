package base_de_donnees;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	private static DBCursor currentCursor;
	/**
	 * Ajouter un message à la base de données.
	 * @param login Le login utilisateur qui souhaite ajouter un message.
	 * @param message Le message à ajouter.
	 * @return Le message tel qu'il est stocké dans la base de données.
	 */
	public static BasicDBObject addMessage(String userID , String message)
	{
		DBCollection msg = DataBase.getMongoCollection("Message");
		BasicDBObject query = new BasicDBObject();
		GregorianCalendar c= new GregorianCalendar();
		BasicDBObject auteur = new BasicDBObject();
		auteur.put("idAuthor", userID);
		auteur.put("login", UserTools.getLoginFromId(userID));
		query.put("author", auteur);
		query.put("user_id", userID);
		query.put("content", message);
		query.put("date", c.getTime());
		query.put("comments", Collections.EMPTY_LIST);
		msg.insert(query);		// Le message est ajouté
		
		// On doit récuperer l'id du message dans la base de données
		DBCursor cursor = msg.find(query);
		DBObject document = cursor.next();
		query.put("id", document.get("_id"));
		return query;
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
		msg.remove(query);
	}
	
	/**
	 * Renvoyer la liste des messages d'un utilisateur et de ses amis
	 * @param login Le login pour lequel il faut chercher la liste des messages.
	 * @param orderAsc Indique si on effectue un trie des messages par date croissante.
	 * @param from Le point de départ de l'affichage des messages.
	 * @param nbMessage Le nombre de message à lister.
	 * @param id_friends La liste des amis pour lesquels il faut récuperer les messages.
	 * @return La liste des messages de l'utilisateur et de ses amis. Ou uniquement la liste des messages de l'utilisateur si id_friends est null. Ou null en cas d'erreur.
	 */
	public static JSONArray listMessage(String id_user, boolean orderAsc, int from, int nbMessage, String[] id_friends) 
	{
		if (from == 0)		// On repart de zéro, il faut effectuer la requête
		{
			DBCollection msg = DataBase.getMongoCollection("Message");
			BasicDBObject query = new BasicDBObject();
			if(id_friends == null)		// On renvoit la liste des messages de l'utilisateur
				query.put("user_id", id_user);
			else						
			{
				ArrayList<String> friends = new ArrayList<String>();
				friends.add(id_user);
				friends.addAll(Arrays.asList(id_friends));
				
				query.put("user_id", new BasicDBObject("$in", friends));
			}
			currentCursor = msg.find(query);
			if (!orderAsc)
				currentCursor.sort(new BasicDBObject("date", -1));
			else
				currentCursor.sort(new BasicDBObject("date", 1));
		}
		// Ici le curseur est prét, on peut itérer
		JSONArray userMessages = new JSONArray();
		try
		{
			int cpt = 0;
			while( (cpt < nbMessage) && (currentCursor.hasNext()))
			{
				JSONObject json = new JSONObject();
				DBObject document = currentCursor.next();
				json.put("content", document.get("content"));
				json.put("author", document.get("author"));
				json.put("date", document.get("date"));
				json.put("id", document.get("_id"));
				json.put("comments", CommentTools.listComment(document.get("_id").toString()));
				
				userMessages.put(json);
				
				cpt++;
			}
			JSONObject ajout = new JSONObject();
			if(!currentCursor.hasNext())
				ajout.put("end", "no");
			else
				ajout.put("end", "yes");
			
			userMessages.put(ajout);
			return userMessages;
		}
		catch(Exception e)
		{
			System.err.println("listMessage : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Liste l'ensemble des messages de l'application.
	 * @param from Le point de départ de l'affichage des messages.
	 * @param nbMessage Le nombre de message à lister.
	 * @return La liste des messages.
	 */
	public static JSONArray listMessage(int from, int nbMessage, boolean orderAsc) 
	{
		if (from == 0)
		{
			DBCollection msg = DataBase.getMongoCollection("Message");
			currentCursor = msg.find();
			
			if (!orderAsc)
				currentCursor.sort(new BasicDBObject("date", -1));
			else
				currentCursor.sort(new BasicDBObject("date", 1));
		}
		
		JSONArray userMessages = new JSONArray();
		try
		{
			int cpt = 0;
			while( (cpt < nbMessage) && (currentCursor.hasNext()))
			{
				JSONObject json = new JSONObject();
				DBObject document = currentCursor.next();
				
				json.put("content", document.get("content"));
				json.put("date", document.get("date"));
				json.put("author", document.get("author"));
				json.put("id", document.get("_id"));
				json.put("comments", CommentTools.listComment( document.get("_id").toString()));
				
				userMessages.put(json);
				
				cpt++;
			}
			JSONObject ajout = new JSONObject();
			if(!currentCursor.hasNext())
				ajout.put("end", "no");
			else
				ajout.put("end", "yes");
			
			userMessages.put(ajout);
			
			return userMessages;
		}
		catch(Exception e)
		{
			System.err.println("listMessage : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
