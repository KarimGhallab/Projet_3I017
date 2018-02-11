package utils;

public class test {

	public static void main(String[] args) 
	{
		//Variables à utiliser pour les tests (Modifiables evidemment)
		String login = "Json";
		String pwd = "Mot de passe hyper chaud à deviner";
		String prenom = "Jason";
		String nom = "Statham";
		String email = "Jason.statham@gmail.com";
		String key = "La cle de Jason";
		String message = "Le message trop cool de Jason";
		
		// Test du service createUser
		System.out.println("Test createUser");
		System.out.println(service.UserServices.createUser(login, pwd, prenom, nom, email) + "\n");
		
		// Test du service login
		System.out.println("Test login");
		System.out.println(service.UserServices.login(login, pwd) + "\n");

		// Test du service logout
		System.out.println("Test logout");
		System.out.println(service.UserServices.logout(key) + "\n");
		
		// Test du service addMessage
		System.out.println("Test addMessage");
		System.out.println(service.MessageServices.addMessage(key, message) + "\n");
	}

}
