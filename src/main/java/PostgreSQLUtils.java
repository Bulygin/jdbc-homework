import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class PostgreSQLUtils {

  private static String connectionURL = "jdbc:postgresql://localhost/";
  private static final String JDBC_DRIVER = "org.postgresql.Driver";
  private static String port = ":5432/";

  /**
   * @param name     name of Data Base
   * @param userName name of user fo Data Base
   * @param password password of user
   * @return сonnection with Data Base
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  private static Connection createBD(String name, String userName, String password)
      throws ClassNotFoundException, SQLException {
    Class.forName(JDBC_DRIVER);
    Connection connection = DriverManager.getConnection(connectionURL, userName, password);
    Statement statement = connection.createStatement();
    statement.executeUpdate("CREATE DATABASE " + name);
    return connection;
  }

  /**
   * @param connection method gets a connection to the database server.
   * @param name       remove this Data Base from server
   * @throws SQLException
   */
  public static void dropDB(Connection connection, String name) throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate("DROP DATABASE IF EXISTS " + name);
  }


  /**
   * This method creates connection to the server by name.
   *
   * @param name     name of Data Base
   * @param userName name of user fo Data Base
   * @param password password of user
   * @return connection with server
   * @throws SQLException
   */
  private static Connection getSQLConnection(String name, String userName, String password)
      throws SQLException {
    String URL = connectionURL + name;
    return DriverManager.getConnection(URL, userName, password);
  }


  /**
   * The method tries to create database. If it throws an exception, it tries to connect to an
   * existing server. Otherwise, it throws an exception.
   *
   * @param name     name of Data Base
   * @param userName name of user fo Data Base
   * @param password password of user
   * @return connection with server
   * @throws RuntimeException
   */
  static Connection getStatement(String name, String userName, String password)
      throws RuntimeException {
    Connection connection = null;
    try {
      connection = PostgreSQLUtils.createBD(name, userName, password);
    } catch (SQLException e) {
      try {
        connection = PostgreSQLUtils.getSQLConnection(name, userName, password);
      } catch (SQLException ex) {
        throw new RuntimeException("Не удалось подключится к БД");
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Не удалось подключится к БД");
    }
    return connection;
  }

}
