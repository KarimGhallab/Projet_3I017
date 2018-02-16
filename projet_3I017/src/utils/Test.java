package utils;

public class Test {

	public static void main(String[] args) 
	{
		// Les infos de l'utilisateur
		String login = "JS";
		String pwd = "mot de passe de toto";
		String prenom = "Jason";
		String nom = "Statham";
		String mail = "toto@chips.fr";
		
		System.out.println(service.UserServices.createUser(login, pwd, prenom, nom, mail).toString());
		System.out.println(base_de_donnees.UserTools.userExists("Utilisateur existe ?" + login));
		
		System.out.println(base_de_donnees.UserTools.insererConnexion(login, pwd));
	}

}
