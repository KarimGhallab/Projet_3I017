package utils;
import org.bson.types.*;

public class Test 
{
	/**
	 * Classe contenant le main pour les tests en local.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println(new ErrorJSON());
		// Les infos de l'utilisateur
		String login = "tata";
		String pwd = "mot de passe de toto";
		String prenom = "Jason";
		String nom = "Statham";
		String mail = "toto@chips.fr";
		String loginFriend = "tata";
		int root = 1;
		int idUser = base_de_donnees.UserTools.getIdUser(loginFriend); 
		int id1;
		int id2;
		
		System.out.println(service.UserServices.createUser(login, pwd, prenom, nom, mail).toString());
		id1 = base_de_donnees.UserTools.getIdUser(login);
		id2= base_de_donnees.UserTools.getIdUser("titi");
		//System.out.println("Insérer connexion : " + base_de_donnees.UserTools.insererConnexion(login, pwd, root) + "\n");
		
		//String key = base_de_donnees.UserTools.getKey(id2);
		//System.out.println("isConnexion : " + base_de_donnees.UserTools.isConnection(key) + "\n");

		System.out.println("search friend friend : " + base_de_donnees.UserTools.searchUserByLogin("t"));
		
		
		//String id_msg= "5a8d9299e4b0893a2034300b";
		
		//base_de_donnees.MessageTools.addMessage(""+id, "à oilp");
		//base_de_donnees.MessageTools.removeMessage(id_msg);
		//System.out.println("Liste des messages : \n" + base_de_donnees.MessageTools.listMessage(""+id, false, 2).toString() + "\n");
	}

}
