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
		System.out.println(service.UserServices.createUser(login, pwd, prenom, nom, email));
		
		// Test du service login
		System.out.println(service.UserServices.login(login, pwd));
		
		// Test du service addMessage
		System.out.println(service.MessageServices.addMessage(key, message));
	}

}
