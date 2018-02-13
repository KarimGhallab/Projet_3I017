package base_de_donnees;

public class UserTools 
{
	
	public static boolean userExists (String login)
	{
		return true;
	}
	
	public static boolean insererUser(String login , String pwd , String prenom , String nom , String email)
	{
		return true;
	}
	public static String insererConnexion(String login , String pwd)
	{
		return "Hello";
	}
	public static boolean checkPwd(String login , String pwd)
	{
		return true;
	}
	public static String getLogin(String key)
	{
		return "toto";
	}
	public static boolean addFriend(String user , String Friend)
	{
		return true;
	}

	public static boolean removeFriend(String idUser, String idFriend)
	{
		return true;
	}
	
	public static String listFriend(String idUser)
	{
		return "Lonely for now";
	}
}
