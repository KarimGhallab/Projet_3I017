package base_de_donnees;

import java.util.ArrayList;
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
	public static boolean addMessage(String login , String message)
	{
		return true;
	}

	/**
	 * Supprimer un message de la base de données.
	 * @param login Le login utilisateur qui souhaite supprimer le message.
	 * @param message Le message à supprimer.
	 * @return True si le message à été supprimé avec succès. False sinon.
	 */
	public static boolean removeMessage(String login, String message) 
	{
		return true;
	}
	
	/**
	 * Renvoyer la liste des messages d'un utilisateur
	 * @param login Le login pour lequel il faut chercher la liste des messages.
	 * @return La liste des message de l'utilisateur. Ou null en cas d'erreur.
	 */
	public static ArrayList<String> listMessage(String login) 
	{
		return new ArrayList<String>();
	}
}
