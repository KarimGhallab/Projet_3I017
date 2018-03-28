package utils;

import java.util.regex.Pattern;

/**
 * Classe des constantes utilisés dans l'application.
 * @author 3772468
 *
 */
public abstract class Data
{
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
	
	public static int CODE_MAIL_MATCHING_ERROR = 1005;
	public static String MESSAGE_MAIL_MATCHING_ERROR = "The two mail addresses do not match together.";
	
	public static int CODE_ERROR_JSON = 101;
	public static String MESSAGE_ERROR_JSON = "Error JSON.";
	
	public static int CODE_USER_ALREADY_EXISTS = 1001;
	public static String MESSAGE_USER_ALREADY_EXISTS = "User already exists.";
	
	public static int CODE_ERROR_DB = -1;
	public static String MESSAGE_ERROR_DB = "Error database.";
	
	public static int CODE_LENGTH_PARAMETER = 1005;
	public static String MESSAGE_LENGTH_PARAMETER = "Error login must have a length of 5, and password must have a length of 8";
	
	public static long DUREE_AVANT_DECO = 60000;//3600000;		//1 Heure
	
	//public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\]", Pattern.CASE_INSENSITIVE);

}
