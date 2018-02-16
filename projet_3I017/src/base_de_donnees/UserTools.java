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
				int idUser = getIdUser(login);
				if(hasSession(idUser))		// Il a déja une session, il faut mettre à jour la date
				{
					if(UserTools.updateDateSession(idUser))
						return UserTools.getKey(idUser);
					else
						return null;
				}
				else
				{
					String skey ="";
					while (keyExists(skey = generate_key()))
					{
						//tkt bro ça marche
					}
					String query = "INSERT INTO session(id_user , skey , root) VALUES(\""+idUser+"\", \""+skey+"\", \""+0+"\");";
					System.out.println("Insertion utilisateur : " + query);
					int res = st.executeUpdate(query);
					if (res == 0)
						return null;
					else
						return skey;
				}
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
	
	public static boolean updateDateSession(int idUser)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "UPDATE session SET sdate = NOW() WHERE id_user = \""+idUser+"\";";
			if(st.executeUpdate(query) == 0)
				return false;
			return true;
		} 
		catch (Exception e) 
		{
			System.err.println("Error updateDateSession : " + e.getMessage());
			return false;
		}
	}
	
	public static String getKey(int idUser)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT skey FROM session WHERE id_user = \""+idUser+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
			{
				return rs.getString(1);
			}
			return null;
		} 
		catch (Exception e) 
		{
			System.err.println("Error getKey : " + e.getMessage());
			return null;
		}
	}
	
	public static boolean hasSession(int idUser)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM session WHERE id_user = \""+idUser+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
			{
				return true;
			}
			return false;
		} 
		catch (Exception e) 
		{
			System.err.println("Error hasSession : " + e.getMessage());
			return false;
		}
	}
	
	public static boolean keyExists(String key)
	{
	
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM session WHERE skey= \""+key+"\";";
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
			return true;
		}
	}
	
	public static boolean checkPwd(String login , String pwd)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM user WHERE login = \""+login+"\" AND pwd = PASSWORD(\""+pwd+"\");";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
			{
				return true;
			}
			return false;
		} 
		catch (Exception e) 
		{
			System.err.println("Error checkPwd : " + e.getMessage());
			return true;
		}
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
