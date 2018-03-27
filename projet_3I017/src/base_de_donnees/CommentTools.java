package base_de_donnees;

import java.util.GregorianCalendar;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Classe contenant les méthodes statiques pour gérer les commentaires de messages avec notre base de données MongDB.
 *
 */
public class CommentTools
{
	/**
	 * Ajoute un commentaire à un message.
	 * @param auteurId L'id de l'auteur du commentaire (ID dans MySQL).
	 * @param idMessage L'id du message commenté (ID dans MongoDB).
	 * @param commentaire Le contenu du commentaire.
	 */
	public static void addComment(String auteurId , String idMessage, String commentaire)
	{
		DBCollection msg = DataBase.getMongoCollection("Message");
		BasicDBObject comments = new BasicDBObject();
		GregorianCalendar c = new GregorianCalendar();
		
		ObjectId objectId = genererObjectId();
		comments.put("id_comment", objectId);
		comments.put("content", commentaire);
		comments.put("date", c.getTime());
		comments.put("idAuthor",auteurId);
		
		BasicDBObject content = new BasicDBObject();
		content.put("comments", comments);
		
		BasicDBObject push = new BasicDBObject();
		push.put("$push", content);
		
		BasicDBObject cond = new BasicDBObject();
		cond.put("_id", new ObjectId(idMessage));
		
		System.out.println("condition : " + cond);
		System.out.println("push : " + push);
		
		msg.update(cond , push);
	}
	
	/**
	 * Liste les commentaires d'un message.
	 * @param idMessage L'id du message (ID dans MongoDB).
	 * @return Une liste des commentaires.
	 */
	public static JSONArray listComment(String idMessage)
	{
		DBCollection msg = DataBase.getMongoCollection("Message");
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(idMessage));
		DBCursor cursor = msg.find(query);
		JSONArray comments = new JSONArray();
		try
		{
			JSONObject json = new JSONObject();
			BasicDBObject auteur = new BasicDBObject();
			DBObject comment = cursor.next();
			String id_user = (String) comment.get("user_id");
			auteur.put("login", UserTools.getLoginFromId(id_user));
			auteur.put("user_id", id_user);
			json.put("comment", comment.get("comments"));
			comments.put(json);
			return comments;
		}
		catch(Exception e)
		{
			System.err.println("listMessage : " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Supprime un commentaire.
	 * @param idCommentaire L'id du commentaire à supprimer (ID dans MongoDB).
	 */
	public static void removeComment(String idCommentaire)
	{
		System.out.println("#################");
		DBCollection msg = DataBase.getMongoCollection("Message");
		BasicDBObject query = new BasicDBObject();
		BasicDBObject condition = new BasicDBObject("id_comment", new ObjectId(idCommentaire));
		query.put("comments", condition);
		BasicDBObject finalQuery = new BasicDBObject();
		finalQuery.put("$pull", query);
		System.out.println("Query : " +finalQuery);
		msg.update(new BasicDBObject(), finalQuery, false, false);
	}
	
	/**
	 * Génére un nouvel ObjectId de commentaire pas encore attribué. 
	 * @return L'ObjectId généré.
	 */
	public static ObjectId genererObjectId()
	{
		DBCollection msg = DataBase.getMongoCollection("Message");
		ObjectId id;
		while(true)
		{
			id = new ObjectId();
			BasicDBObject query = new BasicDBObject();
			query.put("id_comment", new BasicDBObject("$exists", true).put("$ne", id));
			System.out.println(query);
			DBCursor cursor = msg.find(query);
			System.out.println("Taille curseur : " + cursor.size());
			if (cursor.size() != 0)
				break;
		}
		return id;
	}
}
