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
		
		System.out.println("Utilisateur existe : " + base_de_donnees.UserTools.userExists(login));
		System.out.println("Mot de passe correcte : " + base_de_donnees.UserTools.checkPwd(login, pwd));
		
		System.out.println(base_de_donnees.UserTools.insererConnexion(login, pwd));
	}

}
