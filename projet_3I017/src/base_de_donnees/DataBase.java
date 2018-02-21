package base_de_donnees;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

/**
 * Représente une base de données.
 * @author 3772468
 *
 */
public class DataBase
{
	/**  La base de données qui nous fournir la connexion.*/
	private DataSource dataSource;
	
	/** L'état actuel de la base de données. */
	private static DataBase dataBase = null;
	
	/**
	 * Constructeur d'une base de données.
	 * @param name Le nom du driver pour établir la connexion.
	 * @throws SQLException.
	 */
	public DataBase(String name) throws SQLException
	{
		try
		{
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/"+name);
		}
		catch (NamingException e)
		{
			throw new SQLException(name + " is missing in JNDI : " + e.getMessage());
		}
	}
	
	/**
	 * Établir une connexion.
	 * @return La connexion établie.
	 * @throws SQLException.
	 */
	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
	
	/**
	 * Établis une connexion vers notre base de données MySQL.
	 * @return La connexion MySQL établie.
	 * @throws SQLException.
	 * @throws ClassNotFoundException.
	 */
	public static Connection getMySQLConnection() throws SQLException, ClassNotFoundException
	{
		if(DBStatic.mysql_pooling == false)
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			}
			catch (Exception e)
			{
				System.err.println("error getMySQLConnection: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
			return DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" + DBStatic.mysql_db, DBStatic.mysql_username, DBStatic.mysql_password);
		}
		else
		{
			
			if (dataBase == null)
				dataBase = new DataBase("jdbc/db");
			return dataBase.getConnection();
		}
	}
	
	/**
	 * Récupérer une collection MongoDB.
	 * @param collec Le nom de la collection souhaitée.
	 * @return La collection.
	 */
	public static DBCollection getMongoCollection(String collec)
	{
		try 
		{
			Mongo m = new Mongo(DBStatic.mongo_url);
			DB db = m.getDB(DBStatic.mongo_db);
			return db.getCollection(collec);
			
		} catch (UnknownHostException e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}
}
