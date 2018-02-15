package base_de_donnees;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class UserTools 
{
	
	public static boolean userExists (String login)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM user WHERE login= \""+login+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
			{
				return true;
			}
			return false;
		} 
		catch (Exception e) 
		{
			System.err.println("Error userExists : " + e.getMessage());
			return false;
		}
	}
	
	public static boolean insererUser(String login , String pwd , String prenom , String nom , String email)
	{
		try 
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "INSERT INTO user(login, pwd, nom, prenom, mail) VALUES(\""+login+"\", PASSWORD(\""+pwd+"\"), \""+nom+"\", \""+prenom+"\", \""+email+"\");";
			System.out.println("Insertion utilisateur : " + query);
			int res;
			
				res = st.executeUpdate(query);
			 
			if (res == 0)
				return false;
			else
				return true;
		}
		catch (Exception e)
		{
			System.err.println("Error insererUser : " + e.getMessage());
			return false;
		}
	}
	public static String insererConnexion(String login , String pwd)
	{
		try 
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			if(checkPwd(login, pwd))
			{
				String skey ="";
				while (keyExists(skey = generate_key()))
				{
					//tkt bro ça marche
				}
				int idUser = getIdUser(login);
				String query = "INSERT INTO session(id_user , skey , root) VALUES(\""+idUser+"\", \""+skey+"\", \""+0+"\");";
				System.out.println("Insertion utilisateur : " + query);
				int res = st.executeUpdate(query);
				if (res == 0)
					return null;
				else
					return skey;
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			System.err.println("Error insererConnecion : " + e.getMessage());
			return null;
		}
		
	}
	
	public static boolean keyExists(String key)
	{
	
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM user WHERE skey= \""+key+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
			{
				return true;
			}
			return false;
		} 
		catch (Exception e) 
		{
			System.err.println("Error keyExists : " + e.getMessage());
			return false;
		}
	}
	
	public static boolean checkPwd(String login , String pwd)
	{
		return true;
	}
	
	public static String getLogin(String key)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT login FROM user WHERE key= \""+key+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
			{
				return rs.getString(1);
			}
			return null;
		} 
		catch (Exception e) 
		{
			System.err.println("Error getLogin : " + e.getMessage());
			return null;
		}
	}
	
	public static int getIdUser(String login)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT id FROM user WHERE login= \""+login+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
			{
				return rs.getInt(1);
			}
			return -1;
		} 
		catch (Exception e) 
		{
			System.err.println("Error getIdUser : " + e.getMessage());
			return -1;
		}
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
	
	public static boolean isConnection(String key)
	{
		return false;
	}
	
	public static boolean removeConnection(String key)
	{
		return true;
	}
	
	private static String generate_key()
	{
		String key = UUID.randomUUID().toString().replaceAll("-", "");		//Génére une clé de 32 octets.
		System.out.println(key.length());
		return key;
	}
}
