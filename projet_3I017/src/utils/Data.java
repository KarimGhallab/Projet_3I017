package utils;

import java.util.regex.Pattern;

/**
 * Classe des constantes utilisés dans l'application.
 * @author 3772468
 *
 */
public abstract class Data
{
	// Karim Fac
	//public static String FILEPATH = "/users/nfs/Etu8/3772468/twister_images/";

	//Zaki Fac
	//public static String FILEPATH = "/users/nfs/Etu8/3674226/twister_images/";
	
	//Karim casa
	public static String FILEPATH = "/home/coach/twister_images/";
	
	
	public static String FILEPATH_ANON = FILEPATH+"anon.png";
	
	public static String MAIL_ADDRESS = "twister.socialnetwork@gmail.com";
	public static String MAIL_PASSWORD = "3I017Project";
	
	public static int CODE_MISSING_PARAMETERS = -1;
	public static String MESSAGE_MISSING_PARAMETERS = "Missing parameter(s).";
	
	public static int CODE_NOT_CONNECTED = 1005;
	public static String MESSAGE_NOT_CONNECTED = "Not connected.";
	
	public static int CODE_USER_DOES_NOT_EXIST = 1003;
	public static String MESSAGE_USER_DOES_NOT_EXIST = "USER does not exist.";
	
	public static int CODE_FRIEND_DOES_NOT_EXIST = 1002;
	public static String MESSAGE_FRIEND_DOES_NOT_EXIST = "FRIEND does not exist.";
	
	public static int CODE_INCORRECT_LOGIN_PASSWORD = 1004;
	public static String MESSAGE_INCORRECT_LOGIN_PASSWORD = "Incorrect login/password.";
	
	public static int CODE_MAIL_MATCHING_ERROR = 1006;
	public static String MESSAGE_MAIL_MATCHING_ERROR = "The two mail addresses do not match together.";
	
	public static int CODE_ERROR_JSON = 101;
	public static String MESSAGE_ERROR_JSON = "Error JSON.";
	
	public static int CODE_USER_ALREADY_EXISTS = 1001;
	public static String MESSAGE_USER_ALREADY_EXISTS = "User already exists.";
	
	public static int CODE_ERROR_DB = 1000;
	public static String MESSAGE_ERROR_DB = "Error database.";
	
	public static int CODE_LENGTH_PARAMETER = 1008;
	public static String MESSAGE_LENGTH_PARAMETER = "Error login must have a length of 5, and password must have a length of 8";
	
	public static int CODE_MAIL_NOT_FIND = 1007;
	public static String MESSAGE_MAIL_NOT_FIND= "This email address is not associated with any of our user accounts.";
	
	public static int CODE_USER_NOT_FIND = 1009;
	public static String MESSAGE_USER_NOT_FIND= "We cannot find this user.";
	
	public static long DUREE_AVANT_DECO = 3600000;		//1 Heure

}
