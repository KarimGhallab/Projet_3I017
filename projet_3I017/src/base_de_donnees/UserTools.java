package base_de_donnees;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.StringUtils;
/**
 * Classe contenant les méthodes statiques permettant à un utilisateur d'intéragir avec la base de données MySQL.
 *
 */
public class UserTools 
{
	/**
	 * Teste si l'utilisateur existe dans la bd avec son login.
	 * @param login : le login de l'utilisateur.
	 * @return True si l'utilisateur existe dans la base de données. False sinon.
	 */
	public static boolean userExistsLogin (String login)
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
	 * Teste si l'utilisateur existe dans la bd avec son id.
	 * @param id L'id de l'utilisateur dans la base de données.
	 * @return True si l'utilisateur existe dans la base de données. False sinon.
	 */
	
	public static boolean userExistsId (int id)
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
	 * Ajouter un utilisateur dans la base de données.
	 * @param login Le login de l'utilisateur.
	 * @param pwd Le mot de passe de l'utilisateur.
	 * @param prenom Le prenom de l'utilisateur.
	 * @param nom Le nom de l'utilisateur.
	 * @param email L'e-mail de l'utilisateur.
	 * @return boolean True si l'utilisateur à été ajouté dans la base de données. False sinon.
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
	 * Inserer une connexion si l'utilisateur n'est pas connecté. S'il est déjà connecté, on met à jour les données de la connexion. 
	 * @param login Le login de l'utilisateur.
	 * @param pwd Le mot de passe de l'utilisateur.
	 * @return la clé de la connection. Null si la connexion n'a pû être insérée.
	 */
	public static String insererConnexion(String login , String pwd, int root)
	{
		try 
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			if(checkPwd(login, pwd))
			{
				int idUser = getIdUserFromLogin(login);
				if(hasSession(idUser))		// Il a déja une session, il faut mettre à jour la date
				{
					if(UserTools.updateDateSessionById(idUser))
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
					String query = "INSERT INTO session(id_user , skey , root) VALUES(\""+idUser+"\", \""+skey+"\", \""+root+"\");";
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
	 * Mettre a jour les données de connexion de l'utilisateur à partir de sa clé.
	 * @param idUser L'id de l'utilisateur.
	 * @return True si les données ont pû être mises à jour. False sinon.
	 */
	public static boolean updateDateSessionById(int idUser)
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
			System.err.println("Error updateDateSessionById : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Mettre a jour les données de connexion de l'utilisateur à partir de sa clé.
	 * @param key La clé de session de l'utilisateur.
	 * @return True si les données ont pû être mises à jour. False sinon.
	 */
	public static boolean updateDateSessionByKey(String key)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "UPDATE session SET sdate = NOW() WHERE skey = \""+key+"\";";
			if(st.executeUpdate(query) == 0)
				return false;
			return true;
		} 
		catch (Exception e) 
		{
			System.err.println("Error updateDateSessionByKey : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Obtenir la clé de connexion de l'utilisateur a partir de son id.
	 * @param idUser L'id de l'utilisateur.
	 * @return La clé de connexion, ou null en cas de problème.
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
	 * Vérifie si l'utilisateur est connecté a partir de son id.
	 * @param idUser L'id de l'utilisateur.
	 * @return True si l'utilisateur est connecté. False sinon.
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
	 * Verifie si la clé passée en paramètre est deja utilisée.
	 * @param key La clé que l'on doit vérifier.
	 * @return True si la clé existe déjà dans la base de données. False sinon.
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
	 * Vérifie la correspondance (login,pwd).
	 * @param login Le login à vérifier.
	 * @param pwd Le mot de passe à vérifier.
	 * @return True si le couple existe dans la base de données. False sinon.
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
	 * Obtenir le login de l'utilisateur à partir de sa clé.
	 * @param key La clé.
	 * @return Le login de l'utilisateur. False en cas d'erreur avec la base de données.
	 */
	public static String getIdUserFromKey(String key)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT id_user FROM session WHERE skey= \""+key+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
				return rs.getString(1);
			else
				return null;
		} 
		catch (Exception e) 
		{
			System.err.println("Error getLogin : " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Obtenir l'id de l'utilisateur à partir de son login. 
	 * @param login Le login de l'utilisateur.
	 * @return l'id du user, ou -1 en cas de problème avec la base de données.
	 */
	public static int getIdUserFromLogin(String login)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT id FROM user WHERE login= \""+login+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
				return rs.getInt(1);
			else
				return -1;
		} 
		catch (Exception e) 
		{
			System.err.println("Error getIdUser : " + e.getMessage());
			return -1;
		}
	}
	
	/**
	 * Ajouter une entrée dans la table Friend (ajouter ami).
	 * @param idUser L'id de l'utilisateur qui souhaite ajouter un ami.
	 * @param idFriend L'id de l'ami à ajouter.
	 * @return True si l'ajout s'est effectué sans problème. False sinon.
	 */
	public static boolean addFriend(String idUser, String idFriend)
	{
		try
		{
			if(!areFriends(idUser, idFriend))
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
			else
				return false;
		}
		catch (Exception e)
		{
			System.err.println("Error addFriend : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Supprimer un ami dans la table Friend.
	 * @param idUser L'id de l'utilisateur qui souhaite supprimer un ami.
	 * @param idFriend L'id de l'ami à supprimer.
	 * @return True si la suppression s'est effectuée sans problème. False sinon.
	 */

	public static boolean removeFriend(String idUser, String idFriend)
	{
		try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "DELETE FROM friend WHERE id_user=\""+idUser+"\" AND id_friend =\""+idFriend+"\";";
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
		}
		
	}
	
	/**
	 * Lister les amis d'un utilisateur.
	 * @param idUser L'id de l'utilisateur
	 * @return La liste des amis de l'utilisateur.
	 */
	public static JSONArray listFriend(String idUser)
	{
		try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM friend WHERE id_user=\""+idUser+"\";";
			System.out.println("List Ami: " + query);
			ResultSet cursor = st.executeQuery(query);
			JSONArray friendArray = new JSONArray();
			while(cursor.next())
			{
				
				JSONObject json = new JSONObject();
				json.put("user_id",cursor.getString("id_friend"));
				friendArray.put(json);
			}
			
			return friendArray;
		}
		catch (Exception e)
		{
			System.err.println("Error listFriend : " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Test si une connexion existe avec la clé "key". Si necessaire, la date de la clé peut être mise à jour.
	 * @param key La clé à tester
	 * @return boolean True si la clé existe. False sinon.
	 */
	public static boolean isConnection(String key)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT sdate FROM session WHERE skey= \""+key+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())		// Il faut vérifier depuis quand est posée la clé de connexion
			{
				Timestamp tempsSession = rs.getTimestamp(1);
				Timestamp maintenant = new Timestamp(new GregorianCalendar().getTimeInMillis());
				if((maintenant.getTime() - tempsSession.getTime()) >= utils.Data.DUREE_AVANT_DECO)		// Le temps est dépassé
				{
					if (!isRoot(key))		// L'utilisateur n'est pas root, on le déconnecte
					{
						System.out.println("On enlève la connexion");
						removeConnection(key);
						return false;
					}
					else
						return false;
				}
				else if(!isRoot(key))						// Le temps n'est pas dépassé, et l'utilisateur n'est pas root
					return updateDateSessionByKey(key);		//On met donc à jour la date de sa session
				
				else										// Le temps n'est pas dépassé, et l'utilisateur est root
					return true;
			}
			else
				return false;
		} 
		catch (Exception e) 
		{
			System.err.println("Error isConnexion : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Supprimer une connexion.
	 * @param key La clé à supprimer.
	 * @return boolean True si la clé à été supprimée avec succès. False sinon.
	 */
	public static boolean removeConnection(String key)
	{
		try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			
			String query = "DELETE FROM session WHERE skey=\""+key+"\" ;";
			System.out.println("Deconnexion : " + query);
			int res = st.executeUpdate(query);
			if (res == 0)
				return false;
			else
				return true;
		
		}
		catch (Exception e)
		{
			System.err.println("Error removeConnecion : " + e.getMessage());
			return false;
		}
	}
	

	/**
	 * Générer une clé de connexion de 32 caractères.
	 * @return la clé de connexion.
	 */
	private static String generate_key()
	{
		String key = UUID.randomUUID().toString().replaceAll("-", "");		//Génére une clé de 32 octets.
		System.out.println(key.length());
		return key;
	}
	
	/**
	 * Vérifie si une connexion est root.
	 * @param key La clé de connexion
	 * @return true si la connexion est root, false sinon.
	 */
	private static boolean isRoot(String key)
	{
		Connection c;
		try 
		{
			c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT root FROM session WHERE skey= \""+key+"\";";
			ResultSet rs = st.executeQuery(query);
			if(rs.next())		// Il faut vérifier depuis quand est posé la clé de connexion
			{
				int root = rs.getInt(1);
				if(root == 0)
					return false;
				else
					return true;
			}
			else
				return false;
		} 
		catch (Exception e) 
		{
			System.err.println("Error isRoot : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Recherche la liste des utilisateur dont le login ressemble à celui donnée en paramètre.
	 * @param login Le login recherché
	 * @return Une liste des login ressemblant à celui passé en paramètre.
	 */
	public static JSONArray searchUserByLogin(String login)
	{
		try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM user WHERE login LIKE \"%"+login+"%\";";
			System.out.println("UserSearch: " + query);
			ResultSet cursor = st.executeQuery(query);
			JSONArray userArray = new JSONArray();
			while(cursor.next())
			{
				JSONObject json = new JSONObject();
				json.put("user_id",cursor.getString("id"));
				json.put("login",cursor.getString("login"));
				userArray.put(json);
			}
			
			return userArray;
		}
		catch (Exception e)
		{
			System.err.println("Error User_Search : " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Liste tous les amis d'un utilisateur sous forme d'une chaine de caractère.
	 * @param idUser L'id de l'utilisateur dans la base de données MySQL.
	 * @return La liste des amis de l'utilisateur sous la forme suivante : id_ami1-id_ami2-id_ami3-...
	 */
	public static String listFriendString(String idUser)
	{
		try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM friend WHERE id_user=\""+idUser+"\";";
			System.out.println("List Ami: " + query);
			ResultSet cursor = st.executeQuery(query);
			String amis = "";
			while(cursor.next())
				amis += cursor.getString(3) + "-";
			
			return amis;
		}
		catch (Exception e)
		{
			System.err.println("Error listFriend string: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Vérifie si deux utilisateurs sont amis.
	 * @param idUser Le premier utilisateur.
	 * @param idFriend Le second utilisateur.
	 * @return True s'ils sont amis, false sinon.
	 */
	public static boolean areFriends(String idUser, String idFriend)
	{
		try
		{
			Connection c = DataBase.getMySQLConnection();
			Statement st = c.createStatement();
			String query = "SELECT * FROM friend WHERE id_user=\""+idUser+"\" AND id_friend=\""+idFriend+"\";";
			ResultSet cursor = st.executeQuery(query);
			JSONArray friendArray = new JSONArray();
			if(cursor.next())
			{
				return true;
			}
			
			return false;
		}
		catch (Exception e)
		{
			System.err.println("Error areFriend : " + e.getMessage());
			return false;
		}
	}
}
