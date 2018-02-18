package base_de_donnees;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class UserTools 
{
	/**
	 *  teste si l'utilisateur existe dans la bd avec son login
	 * @param login : le login de l'utilisateur
	 * @return boolean 
	 */
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
				return true;
			return false;
		} 
		catch (Exception e) 
		{
			System.err.println("Error userExists : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * teste si l'utilisateur existe dans la bd avec son id
	 * @param id
	 * @return
	 */
	
	public static boolean userExists (int id)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM user WHERE id = \""+id+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
				return true;
			return false;
		} 
		catch (Exception e) 
		{
			System.err.println("Error userExists : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Ajouter un uutilisateur dans la base de données
	 * @param login 
	 * @param pwd
	 * @param prenom
	 * @param nom
	 * @param email
	 * @return boolean
	 */
	
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
	
	/**
	 * inserer une connexion s'il est pas connecté et mettre a jour les données de la connxion s'il est deja connecté 
	 * @param login
	 * @param pwd
	 * @return la clé de la connection
	 */
	public static String insererConnexion(String login , String pwd)
	{
		try 
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			if(checkPwd(login, pwd))
			{
				int idUser = getIdUser(login);
				if(hasSession(idUser))		// Il a dÃ©ja une session, il faut mettre Ã  jour la date
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
						//tkt bro Ã§a marche
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
	
	/**
	 * mettre a jour les données de connexion de l'utilisateur
	 * @param idUser
	 * @return boolean
	 */
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
	
	/**
	 * obtenir la clé de connexion de l'utilisateur a partir de son id
	 * @param idUser
	 * @return la clé de connexion
	 */
	
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
	
	/**
	 * verifie si l'utilisateur est connecté a partir de son id
	 * @param idUser
	 * @return boolean
	 */
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
	
	/**
	 * verifie si la clé passé en parametre est deja utilisé
	 * @param key
	 * @return boolean
	 */
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
	
	/**
	 * vérifie la correspondance (login,pwd)
	 * @param login
	 * @param pwd
	 * @return boolean
	 */
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
	
	/**
	 * obtenir le login du user a partir de son id
	 * @param key
	 * @return le login
	 */
	
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
	
	/**
	 * obtenir le id du user a partir de son login 
	 * @param login
	 * @return l'id du user
	 */
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
	
	/**
	 * ajouter une entrée dans la table Friend (ajouter ami) 
	 * @param idUser 
	 * @param idFriend
	 * @return boolean
	 */
	public static boolean addFriend(String idUser, String idFriend)
	{
		try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "INSERT INTO friend(id_user, id_friend) VALUES(\""+idUser+"\", \""+idFriend+"\");";
			System.out.println("Ajout ami : " + query);
			int res = st.executeUpdate(query); 
			if (res == 0)
				return false;
			else
				return true;
		}
		catch (Exception e)
		{
			System.err.println("Error addFriend : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * supprimer un ami dans la table Friend
	 * @param idUser
	 * @param idFriend
	 * @return boolean
	 */

	public static boolean removeFriend(String idUser, String idFriend)
	{
		/*try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "DELETE FROM friend(id_user, id_friend) WHERE idUser=\""+idUser+"\" AND idFiend =\""+idFriend+"\";";
			System.out.println("Supprimer ami : " + query);
			int res = st.executeUpdate(query); 
			if (res == 0)
				return false;
			else
				return true;
		}
		catch (Exception e)
		{
			System.err.println("Error RemoveFriend : " + e.getMessage());
			return false;
		}*/
		
		return true;
	}
	
	/**
	 * lister les amis d'un utilisateur
	 * @param idUser
	 * @return 
	 */
	public static ArrayList<String> listFriend(String idUser)
	{
		/*try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM friend WHERE idUser=\""+idUser+"\";";
			System.out.println("List Ami: " + query);
			ResultSet cursor = st.executeQuery(query);
			ArrayList<String> listFriend = new ArrayList<String>();
			while(cursor.next())
			{
				listFriend.add(cursor.getString("idFriend"));
			}
			
			return listFriend;
		}
		catch (Exception e)
		{
			System.err.println("Error addFriend : " + e.getMessage());
			return null;
		}*/
		return null;
	}
	
	/**
	 * test si une connexion existe avec la clé "key"
	 * @param key
	 * @return boolean
	 */
	public static boolean isConnection(String key)
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
			System.err.println("Error getIdUser : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * supprimer une connexion
	 * @param key
	 * @return boolean
	 */
	
	public static boolean removeConnection(String key)
	{
		/*try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			
			if(isConnection(key))
			{
	
					String query = "DELETE FROM connection WHERE skey=\""+key+"\" ;";
					System.out.println("Deconnexion : " + query);
					int res = st.executeUpdate(query);
					if (res == 0)
						return false;
					else
						return true;
				
			}
			else
			{
				return true;
			}
		}
		catch (Exception e)
		{
			System.err.println("Error insererConnecion : " + e.getMessage());
			return false;
		}*/

		return true;
	}
	

	/**
	 * generer la clé de connexion a partir de  l'id du user
	 * @param idUser
	 * @return la clé de connexion
	 */
	private static String generate_key()
	{
		String key = UUID.randomUUID().toString().replaceAll("-", "");		//GÃ©nÃ©re une clÃ© de 32 octets.
		System.out.println(key.length());
		return key;
	}
}
