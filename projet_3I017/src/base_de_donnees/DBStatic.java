package base_de_donnees;

/**
 * Classe contenant les informations necessaires à la connexion vers les différentes bases de données (MySQL et MongoDB).
 *
 */
public class DBStatic 
{	
	/** Booléen indiquant si l'on utilise un pooling de connexion ou non pour la base de données MySQL. */
	public static boolean mysql_pooling = false;
	
	/** L'hôte de la base de données pour la base de données MySQL. */
	public static String mysql_host = "localhost";
	
	/** Le nom de la base de données pour la base de données MySQL. */
	public static String mysql_db = "ghallab_fodil";
	
	/** Le nom d'utilisateur pour la connexion vers la base de données MySQL. */
	public static String mysql_username = "root";
	
	/** /** Le mot de passe pour la connexion vers la base de données MySQL. */
	public static String mysql_password = "root";
}
