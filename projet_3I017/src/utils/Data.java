package utils;

public abstract class Data
{
	public static int CODE_MISSING_PARAMETERS = -1;
	public static String MESSAGE_MISSING_PARAMETERS = "Missing parameter(s).";
	
	public static int CODE_NOT_CONNECTED = 1005;
	public static String MESSAGE_NOT_CONNECTED = "Not connected.";
	
	public static int CODE_USER_DOES_NOT_EXIST = 1003;
	public static String MESSAGE_USER_DOES_NOT_EXIST = "User does not exist.";
	
	public static int CODE_INCORRECT_LOGIN_PASSWORD = 1004;
	public static String MESSAGE_INCORRECT_LOGIN_PASSWORD = "Incorrect login/password.";
	
	public static int CODE_ERROR_JSON = 101;
	public static String MESSAGE_ERROR_JSON = "Error JSON.";
	
	public static int CODE_USER_ALREADY_EXISTS = 1001;
	public static String MESSAGE_USER_ALREADY_EXISTS = "User already exists.";
	
	public static int CODE_ERROR_DB = -1;
	public static String MESSAGE_ERROR_DB = "Error database.";
}
