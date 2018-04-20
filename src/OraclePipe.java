import java.sql.*;
import java.util.Scanner;

public class OraclePipe {

    private Connection connection;

    public OraclePipe(){
        if((this.connection = connect()) == null){
            System.out.println("---YOU DIDN'T CONNECT TO THE DATABASE---");
        }
    }

    public static Connection connect(){
        Connection database;
        // Assign username and password
        String db_host = "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl";
        String db_username = "alrobbertz";
        String db_password = "Buffalo4";

        // Attempt to connect to the database
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException driver_error) {
            System.out.println("Oracle JDBC Driver Not Found");
            System.out.println(driver_error.getMessage() + ".");
            driver_error.printStackTrace();
            return null;
        }

        try {
            database = DriverManager.getConnection(db_host, db_username, db_password);
        }
        catch (SQLException connection_error) {
            System.out.println("Database connection was unsuccessful.");
            System.out.println(connection_error.getMessage() + ".");
            return null;
        }

        return database;

    }

    public void close(){
        try {
            connection.close();
        }
        catch (SQLException connection_error) {
            System.out.println("---FAILED TO CLOSE THE CONNECTION---");
            System.out.println(connection_error.getMessage() + ".");
            return;
        }
    }

    public void dropTables() {
        execute("DROP TABLE Edge");
        execute("DROP TABLE Permissions");
        execute("DROP TABLE Service");
        execute("DROP TABLE Employee");
        execute("DROP TABLE Login");
        execute("DROP TABLE Manage");
        execute("DROP TABLE Node");

    }

    public void createTables() {
        String script = "CREATE TABLE Node\n" +
                "(nodeID VARCHAR(32),\n" +
                " longName VARCHAR(255),\n" +
                " shortName VARCHAR(255),\n" +
                " coordX2D REAL,\n" +
                " coordY2D REAL,\n" +
                " coordX3D REAL,\n" +
                " coordY3D REAL,\n" +
                " nodeType CHAR(4),\n" +
                " building VARCHAR(255),\n" +
                " floor VARCHAR(10),\n" +
                "  CONSTRAINT p_key_node PRIMARY KEY (nodeID));\n" +
                "\n" +
                "CREATE INDEX node_id_index ON Node(nodeID);\n" +
                "\n" +
                "CREATE TABLE Edge\n" +
                "(edgeID\t\t\t\t\t\t VARCHAR(65),\n" +
                " node_id_one \t\t\t VARCHAR(32),\n" +
                " node_id_two \t\t\t VARCHAR(32),\n" +
                "  CONSTRAINT p_key_edge PRIMARY KEY (edgeID),\n" +
                "  CONSTRAINT f_key_node_one FOREIGN KEY (node_id_one) REFERENCES Node(nodeID),\n" +
                "  CONSTRAINT f_key_node_two FOREIGN KEY (node_id_two) REFERENCES Node(nodeID));\n" +
                "\n" +
                "CREATE INDEX edge_id_index ON Edge(edgeID);\n" +
                "\n" +
                "\n" +
                "CREATE TABLE Employee\n" +
                "(employeeID\t\t\t\t VARCHAR(32),\n" +
                " first_name\t\t\t\t VARCHAR(32),\n" +
                " last_name  \t\t\t VARCHAR(32),\n" +
                " title\t\t\t\t\t\t VARCHAR(32),\n" +
                "  CONSTRAINT p_key_employee PRIMARY KEY (employeeID));\n" +
                "\n" +
                "CREATE INDEX employee_id_index ON Employee(employeeID);\n" +
                "\n" +
                "CREATE TABLE Login\n" +
                "(employeeID VARCHAR(32),\n" +
                " username   VARCHAR(32),\n" +
                " password   VARCHAR(32),\n" +
                " CONSTRAINT p_key_login PRIMARY KEY (employeeID),\n" +
                " CONSTRAINT f_key_employee FOREIGN KEY (employeeID) REFERENCES Employee(employeeID));\n" +
                "\n" +
                "CREATE INDEX login_id_index ON Login(username, password);\n" +
                "\n" +
                "CREATE TABLE Manage\n" +
                "(employeeID_Manager \tVARCHAR(32),\n" +
                " employeeID_Employee  VARCHAR(32),\n" +
                "  CONSTRAINT p_key_manage PRIMARY KEY (employeeID_Manager, employeeID_Employee),\n" +
                "  CONSTRAINT f_key_manage FOREIGN KEY (employeeID_Manager) REFERENCES Employee(employeeID),\n" +
                "  CONSTRAINT f_key_manage2 FOREIGN KEY (employeeID_Employee) REFERENCES  Employee(employeeID));\n" +
                "\n" +
                "CREATE INDEX manager_id_index ON Manage(employeeID_Manager, employeeID_Employee);\n" +
                "\n" +
                "CREATE TABLE Permissions\n" +
                "(employeeID\t\t\t\t VARCHAR(32),\n" +
                " serviceType\t\t\t VARCHAR(32),\n" +
                "  CONSTRAINT  f_key_permissions FOREIGN KEY  (employeeID) REFERENCES  Employee(employeeID),\n" +
                "  CONSTRAINT  p_key_permissions PRIMARY KEY (employeeID, serviceType));\n" +
                "\n" +
                "CREATE INDEX permissions_emp_index ON Permissions(employeeID);\n" +
                "\n" +
                "CREATE TABLE Service\n" +
                "(serviceID \t\t\t\tVARCHAR(32),\n" +
                " kioskID\t\t\t\t\tVARCHAR(32),\n" +
                " request_time\t\t\tTIMESTAMP,\n" +
                " serviceType\t\t\tVARCHAR(32),\n" +
                " destinationNode  VARCHAR(32),\n" +
                " completed\t\t\t\tBOOLEAN,\n" +
                " assignedTo \t\t\tVARCHAR(32),\n" +
                " hoursToComplete\tREAL,\n" +
                " description\t\t\tVARCHAR(512),\n" +
                " service_name     VARCHAR(32),\n" +
                "  CONSTRAINT p_key_service PRIMARY KEY (serviceID),\n" +
                "  CONSTRAINT f_key_service_node FOREIGN KEY (destinationNode) REFERENCES Node(nodeID));\n" +
                "\n" +
                "CREATE INDEX service_type_index ON Service(serviceType);\n" +
                "CREATE INDEX service_assigned_index ON Service(assignedTo);";
        String[] statements = parseSQLScript(script);
        for(String statement : statements) {
            executeCreate(statement);

        }
    }

    public void executeCreate(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("---CREATED TABLE---");
        } catch (SQLException e) {
            System.out.println("---FAILED TO CREATE TABLE---");
        }
    }

    //Send sql query and connection
    public void execute(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("--- FAILED IN execute() ---");
            e.printStackTrace();
        }
    }

    public String[] parseSQLScript(String script) {
        String[] statements = script.split(";");
        return statements;

    }

}
