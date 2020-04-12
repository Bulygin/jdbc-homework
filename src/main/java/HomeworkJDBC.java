import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class HomeworkJDBC {

  private static ResultSet result;
  private static int count = 0;
  private static double max;

  /**
   * The method fills the database with test data.
   *
   * @param connection сonnection with Data Base
   * @return false if data in table employee exist, else creates data and returns true
   * @throws Exception
   */
  static boolean fillingBaseWithTestData(Connection connection) throws Exception {
    Class.forName("org.postgresql.Driver");
    Statement statement = connection.createStatement();

    //Checking for the existence of data in the table
    ResultSet result = statement.executeQuery("SELECT * FROM employee");
    if (result != null) {
      return false;
    }

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

    statement.execute("CREATE TABLE department(id INT PRIMARY KEY generated always as identity,"
        + "nameDepartment VARCHAR (100) NOT NULL, idBoss INT NOT NULL)");

    statement.execute("INSERT INTO department (nameDepartment, idBoss) " + "VALUES ('Develop', 2)");

    statement.execute(
        "INSERT INTO department (nameDepartment, idBoss) " + "VALUES ('Administration', 5)");

    statement
        .execute("INSERT INTO department (nameDepartment, idBoss) " + "VALUES ('Accounting', 6)");

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
    return true;
  }

  /**
   * Список  сотрудников указанного подразделения с сортировкой по зарплате
   *
   * @param statement
   * @param department указанное подразделение
   * @return
   * @throws Exception
   */
  static int getNumEmployeeSortedSalary(Statement statement, String department) throws Exception {
    String query =
        "SELECT employee.name, employee.salary " + "FROM employee JOIN depart_of_employee ON "
            + "employee.id = depart_of_employee.id_Employee JOIN department ON "
            + "depart_of_employee.id_Department = department.id "
            + "WHERE department.nameDepartment = '" + department + "' \n"
            + "ORDER BY employee.salary DESC";
    result = statement.executeQuery(query);
    System.out.println("-------------------------------");
    while (result.next()) {
      count++;
      System.out.println(result.getString(1) + "\t" + result.getString(2));
      max = Double.MAX_VALUE;
      double salary = Double.parseDouble(result.getString(2));
      if (salary < max) {
        max = salary;
      } else {
        throw new Exception("Table not sorted");
      }
    }
    System.out.println("-------------------------------");
    int numOFRows = count;
    count = 0;
    return numOFRows;
  }


  /**
   * Список сотрудников указанного подразделения с сортировкой по ФИО
   *
   * @param statement
   * @param department указанное подразделение
   * @return
   * @throws Exception
   */
  static int getNumEmployeeSortedName(Statement statement, String department) throws Exception {
    String query =
        "SELECT employee.name, employee.position " + "FROM employee JOIN depart_of_employee ON "
            + "employee.id = depart_of_employee.id_Employee JOIN department ON "
            + "depart_of_employee.id_Department = department.id "
            + "WHERE department.nameDepartment = '" + department + "' \n"
            + "ORDER BY employee.name";
    result = statement.executeQuery(query);
    double max = Double.MAX_VALUE;
    System.out.println("-------------------------------");
    while (result.next()) {
      System.out.println(result.getString(1) + "\t" + result.getString(2));
      count++;
    }
    System.out.println("-------------------------------");
    int numOFRows = count;
    count = 0;
    return numOFRows;
  }


  /**
   * Список сотрудников у который оклад больше, чем у заданного
   *
   * @param statement
   * @param name
   * @return
   * @throws SQLException
   */
  static int getNumEmployeeSalaryMore(Statement statement, String name) throws SQLException {
    String query =
        "SELECT employee.name, employee.salary " + "FROM employee " + "WHERE employee.salary > "
            + "(" + " SELECT employee.salary " + "FROM employee " + "WHERE employee.name = '" + name
            + "' " + " )";
    result = statement.executeQuery(query);
    System.out.println("-------------------------------");
    while (result.next()) {
      count++;
      System.out.println(result.getString(1) + "\t" + result.getString(2));
    }
    System.out.println("-------------------------------");
    int numOFRows = count;
    count = 0;
    return numOFRows;
  }

  /**
   * Если ввели ФИО рядового сотрудника - то вывести только его зарплату. Если ввели начальника
   * подразделения - вывести список всех его подчинённых с указанием зарплаты и сортировкой по ФИО
   *
   * @param statement
   * @param name      ФИО сотрудника
   * @return
   * @throws SQLException
   */
  static int getSalaryByName(Statement statement, String name) throws SQLException {
    String query =
        "SELECT employee.name, employee.salary " + " FROM employee JOIN depart_of_employee ON "
            + "employee.id = depart_of_employee.id_Employee JOIN department ON "
            + "depart_of_employee.id_Department = department.id "
            + "WHERE employee.id = department.idBoss AND employee.name = '" + name + "'";
    result = statement.executeQuery(query);

    while (result.next()) {
      count++;
    }
    System.out.println("-------------------------------");
    if (count == 1) {
      query = "SELECT employee.name, employee.salary " + "FROM employee JOIN depart_of_employee ON "
          + "employee.id = depart_of_employee.id_Employee JOIN department ON "
          + "depart_of_employee.id_Department = department.id " + "WHERE id_Department = ("
          + "select depart_of_employee.id_Department " + "from employee JOIN depart_of_employee ON "
          + "employee.id = depart_of_employee.id_Employee " + "where employee.name = '" + name
          + "')";
      result = statement.executeQuery(query);
      count = 0;
      while (result.next()) {
        count++;
        System.out.println(result.getString(1) + "\t" + result.getString(2));
      }
    } else {
      count = 0;
      query =
          "SELECT employee.name, employee.salary " + "FROM employee JOIN depart_of_employee ON  "
              + "employee.id = depart_of_employee.id_Employee JOIN department ON "
              + "depart_of_employee.id_Department = department.id " + "WHERE employee.name = '"
              + name + "'";
      result = statement.executeQuery(query);
      while (result.next()) {
        count++;
        System.out.println(result.getString(1) + "\t" + result.getString(2));
      }
    }
    System.out.println("-------------------------------");
    int numOFRows = count;
    count = 0;
    return numOFRows;
  }
}
