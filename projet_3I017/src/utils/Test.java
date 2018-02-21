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
		String login = "JS";
		String pwd = "mot de passe de toto";
		String prenom = "Jason";
		String nom = "Statham";
		String mail = "toto@chips.fr";
		String loginFriend = "toto";
		int root = 1;
		int idFriend = base_de_donnees.UserTools.getIdUser(loginFriend); 
		int id;
		
		System.out.println(service.UserServices.createUser(login, pwd, prenom, nom, mail).toString());
		id = base_de_donnees.UserTools.getIdUser(login);
		
		base_de_donnees.UserTools.insererConnexion(login, pwd, root);
		
		//base_de_donnees.MessageTools.addMessage("3", "à oilp");
		String id_msg= "5a8d9299e4b0893a2034300b";
		//base_de_donnees.MessageTools.removeMessage(id_msg);
		System.out.println(base_de_donnees.MessageTools.listMessage(""+id, false, 2).toString());
		
		System.out.println("Utilisateur existe : " + base_de_donnees.UserTools.userExists(login));
		System.out.println("Mot de passe correcte : " + base_de_donnees.UserTools.checkPwd(login, pwd));
		
		//addFriend
		/*System.out.println(base_de_donnees.UserTools.insererConnexion(login, pwd));
		System.out.println("Ajout ami : " + base_de_donnees.UserTools.addFriend(id, idFriend));*/
	}

}
