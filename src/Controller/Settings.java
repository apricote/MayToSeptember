package Controller;

/**
 * All of the configurations the app needs to run
 */
public class Settings {
    public static LoggingLevel LOGGING_LEVEL = LoggingLevel.INFO;

    public static String MYSQL_SERVER_ADDRESS = "localhost";
    public static String MYSQL_DATABASE = "maytoseptember";
    public static String MYSQL_USERNAME = "facharbeit";
    public static String MYSQL_PASSWORD = "facharbeit";

    public static String JDBC_DRIVER_STRING = "com.mysql.jdbc.Driver";
    public static String JDBC_CONNECTION_STRING = "jdbc:mysql://" + MYSQL_SERVER_ADDRESS + "/" + MYSQL_DATABASE;
}
