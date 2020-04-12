import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class PostgreSQLUtils {

  private static String connectionURL = "jdbc:postgresql://localhost/";
  private static final String JDBC_DRIVER = "org.postgresql.Driver";
  private static String port = ":5432/";

  static void createBD(String name, String userName, String password)
      throws ClassNotFoundException, SQLException {
    Class.forName(JDBC_DRIVER);
    String SQL = "CREATE DATABASE " + name;
    Connection connection = DriverManager.getConnection(connectionURL, userName, password);
    Statement statement = connection.createStatement();
    statement.executeUpdate(SQL);
  }

  static Connection getSQLConnection(String dbName, String userName, String password)
      throws SQLException {

    String URL = connectionURL + dbName;
    return DriverManager.getConnection(URL, userName, password);
  }

}
