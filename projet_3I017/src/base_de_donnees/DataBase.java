package base_de_donnees;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataBase
{
	private DataSource dataSource;
	private static DataBase dataBase = null;
	
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
	
	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
	
	public static Connection getMySQLConnection() throws SQLException
	{
		if(DBStatic.mysql_pooling == false)
			return DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" + DBStatic.mysql_db, DBStatic.mysql_username, DBStatic.mysql_password);
		else
		{
			
			if (dataBase == null)
				dataBase = new DataBase("jdbc/db");
			return dataBase.getConnection();
		}
			
	}
}
