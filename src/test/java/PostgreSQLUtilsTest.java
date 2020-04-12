import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PostgreSQLUtilsTest {

  private static Connection connection = null;
  private static Statement statement = null;

  @Before
  public void setUp() throws Exception {
    PostgreSQLUtils.createBD("epamhomeworkdb", "postgres", "root");
    connection = PostgreSQLUtils.getSQLConnection("epamhomeworkdb", "postgres", "root");
    statement = connection.createStatement();
    Class.forName("org.postgresql.Driver");

    statement.execute(
        "CREATE TABLE employee( " + "id INT PRIMARY KEY generated always as identity, "
            + "name VARCHAR (100) NOT NULL, " + "salary DECIMAL(18, 2) NOT NULL, "
            + "position VARCHAR (100) NOT NULL)");

    statement.execute("INSERT INTO employee (name, salary, position) "
        + "VALUES ('Mikael Scott', 60000, 'Developer')");

    statement.execute("INSERT INTO employee (name, salary, position) "
        + "VALUES ('Alan Moore', 120000, 'Team lead')");

    statement.execute("INSERT INTO employee (name, salary, position) "
        + "VALUES ('Scott Pilgrim', 50000, 'QA tester')");

    statement.execute("INSERT INTO employee (name, salary, position) "
        + "VALUES ('Ted Mosby', 80000, 'Architect')");

    statement.execute("INSERT INTO employee (name, salary, position) "
        + "VALUES ('Geralt of Rivea', 100000, 'Head manager')");

    statement.execute("INSERT INTO employee (name, salary, position) "
        + "VALUES ('Ivan Grozny', 60000, 'Chief accountant')");

    statement.execute("INSERT INTO employee (name, salary, position) "
        + "VALUES ('Dr. Manhattan', 40000, 'Accountant')");

    statement.execute(
        "CREATE TABLE department(" + "id INT PRIMARY KEY generated always as identity,"
            + "nameDepartment VARCHAR (100) NOT NULL)");

    statement.execute("INSERT INTO department (nameDepartment) " + "VALUES ('Develop')");

    statement.execute("INSERT INTO department (nameDepartment) " + "VALUES ('Administration')");

    statement.execute("INSERT INTO department (nameDepartment) " + "VALUES ('Accounting')");

    statement.execute("CREATE TABLE depart_of_employee( " + "id_Employee INT NOT NULL PRIMARY KEY, "
        + "id_Department INT NOT NULL," + "FOREIGN KEY (id_Employee) REFERENCES employee (id), "
        + "FOREIGN KEY (id_Department) REFERENCES department (id)) ");

    statement.execute("INSERT INTO depart_of_employee " + "VALUES (1, 1)");
    statement.execute("INSERT INTO depart_of_employee " + "VALUES (2, 1)");
    statement.execute("INSERT INTO depart_of_employee " + "VALUES (3, 1)");
    statement.execute("INSERT INTO depart_of_employee " + "VALUES (4, 2)");
    statement.execute("INSERT INTO depart_of_employee " + "VALUES (5, 2)");
    statement.execute("INSERT INTO depart_of_employee " + "VALUES (6, 3)");
    statement.execute("INSERT INTO depart_of_employee " + "VALUES (7, 3)");
  }

  @After
  public void tearDown() throws Exception {
    statement.close();
    connection.close();
  }

  @Test
  public void firstTask_ExecuteQuery_mustBeSorted() throws Exception {
    connection = PostgreSQLUtils.getSQLConnection("epamhomeworkdb", "postgres", "root");
    statement = connection.createStatement();
    String department = "Administration";
    String query = "SELECT employee.name, employee.salary, department.nameDepartment "
        + "FROM employee JOIN depart_of_employee ON "
        + "employee.id = depart_of_employee.id_Employee JOIN department ON "
        + "depart_of_employee.id_Department = department.id "
        + "WHERE department.nameDepartment = '" + department + "' \n"
        + "ORDER BY employee.salary DESC";
    ResultSet result = statement.executeQuery(query);

    int count = 0;
    double max = Double.MAX_VALUE;
    while (result.next()) {
      count++;
      System.out
          .println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getString(3));
      double salary = Double.parseDouble(result.getString(2));
      if (salary < max) {
        max = salary;
      } else {
        throw new Exception("Table not sorted");
      }
    }
  }
/*
  @Test
  public void test() {

  }*/
}