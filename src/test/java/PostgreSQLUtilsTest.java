import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PostgreSQLUtilsTest {

  private static Connection connection;
  private static Statement statement;

  @BeforeClass
  public static void createDB() throws Exception {
    connection = PostgreSQLUtils.getStatement("epamhomeworkdb", "postgres", "root");
    HomeworkJDBC.fillingBaseWithTestData(connection);
  }

  @AfterClass
  public static void dropDb() throws SQLException {
    PostgreSQLUtils.dropDB(connection, "epamhomeworkdb");
  }

  @Test
  public void getEmployeeSortedSalary_ExecuteQuery_NumberMustBeSame() throws Exception {
    statement = connection.createStatement();
    String department = "Develop";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSortedSalary(statement, department), 3);
    department = "Administration";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSortedSalary(statement, department), 2);
    department = "Accounting";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSortedSalary(statement, department), 2);
    statement.close();
  }

  @Test
  public void getEmployeeSortedName_ExecuteQuery_NumberMustBeSame() throws Exception {
    statement = connection.createStatement();
    String department = "Develop";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSortedName(statement, department), 3);
    department = "Administration";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSortedName(statement, department), 2);
    department = "Accounting";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSortedName(statement, department), 2);
    statement.close();
  }

  @Test
  public void getNumEmployeeSalaryLess_ExecuteQuery_NumberMustBeSame() throws Exception {
    statement = connection.createStatement();
    String name = "Mikael Scott";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSalaryMore(statement, name), 3);
    name = "Dr. Manhattan";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSalaryMore(statement, name), 6);
    name = "Alan Moore";
    Assert.assertEquals(HomeworkJDBC.getNumEmployeeSalaryMore(statement, name), 0);
  }

  @Test
  public void getSalaryByName_ExecuteQuery_NumberMustBeSame() throws Exception {
    statement = connection.createStatement();
    String name = "Ted Mosby";
    Assert.assertEquals(HomeworkJDBC.getSalaryByName(statement, name), 1);
    name = "Alan Moore";
    Assert.assertEquals(HomeworkJDBC.getSalaryByName(statement, name), 3);
  }
}