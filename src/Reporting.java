/*
1- Log in to CCC machine
  
2- Set environment variables
> source /cs/bin/oracle-setup

3- Set CLASSPATH for java
> export CLASSPATH=./:/usr/local/oracle11gr203/product/11.2.0/ db_1/jdbc/lib/ojdbc6.jar

4- Write your java code (say file name is OracleTest.java) and then compile it 
> /usr/local/bin/javac OracleTest.java

5- Run it 
> /usr/local/bin/java OracleTest
*/ 

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class Reporting {

  public static void oldMain(String args[]) {
    // Create scanner for future user
    Scanner reader = new Scanner(System.in);
    Connection database;
  
    // Ensure the user provides at minimum a username and password
    if (args.length < 2) {
      System.out.println("Provide a database username and password.");
      return;
    }
    
    // Assign username and password
    String username = args[0];
    String password = args[1];
    
    // Attempt to connect to the database
    String db_host = "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl";
    String db_username = username;
    String db_password = password;

    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
    } 
    catch (ClassNotFoundException driver_error) {
      System.out.println("Oracle JDBC Driver Not Found");
      System.out.println(driver_error.getMessage() + ".");
      driver_error.printStackTrace();
      return;
    }

    try {
      database = DriverManager.getConnection(db_host, db_username, db_password);
    }
    catch (SQLException connection_error) {
      System.out.println("Database connection was unsuccessful.");
      System.out.println(connection_error.getMessage() + ".");
      return;
    }
    
    // Use an additional option if it has been provided
    int provided_option = 0;
    if (args.length > 2) {
      provided_option = Integer.parseInt(args[2]);
    }
    
    // Run appropriate methods
    if (provided_option == 0) {
      System.out.println("1. Report patients basic information.");
      System.out.println("2. Report doctors basic information.");
      System.out.println("3. Report admissions information.");
      System.out.println("4. Update admissions payment.");
    }
    else if (provided_option == 1) {
      System.out.println("Enter Patient SSN: ");
      String patient_ssn = reader.next();
      option1(database, patient_ssn);
    }
    else if (provided_option == 2) {
      System.out.println("Doctor ID: ");
      int doctor_id = reader.nextInt();
      option2(database, doctor_id);
    }
    else if (provided_option == 3) {
      System.out.println("Admission number: ");
      int admission_number = reader.nextInt();
      option3(database, admission_number);
    }
    else if (provided_option == 4) {
      System.out.println("Admission number: ");
      int admission_number = reader.nextInt();
      System.out.println("New total payment: ");
      int new_total_payment = reader.nextInt();
      option4(database, admission_number, new_total_payment);
    }
    else {
      System.out.println("Provide an option in the range [0, 4].");
    }

    try {
      database.close();
    }
    catch (SQLException connection_error) {
      System.out.println(connection_error.getMessage() + ".");
      return;
    }
  }
  
  // Query the patient table and print information
  public static void option1(Connection database, String patient_ssn) {
    try {
      Statement query_statement = database.createStatement();
      String query = "select ssn, first_name, last_name, address from Patient P where ssn = " + patient_ssn;
      ResultSet rset = query_statement.executeQuery(query);
      if(rset.next()){
        System.out.println("Patient SSN: " + rset.getInt("ssn"));
        System.out.println("Patient First Name: " + rset.getString("first_name"));
        System.out.println("Patient Last Name: " + rset.getString("last_name"));
        System.out.println("Patient Address: " + rset.getString("address"));
      } else {
        System.out.println("Query Returned No Results.");
      }
      rset.close();
      query_statement.close();
    }
    catch (SQLException statement_error) {
      System.out.println("Query was unsuccessful.");
      System.out.println(statement_error.getMessage() + ".");
      return;
    }
  }
  
  // Query the doctor table and print information
  public static void option2(Connection database, int doctor_id) {
    try {
      Statement query_statement = database.createStatement();
      String query = "select id, first_name, last_name, gender from Doctor D where id = " + doctor_id;
      ResultSet rset = query_statement.executeQuery(query);
      if(rset.next()){
        System.out.println("Doctor ID: " + rset.getInt("id"));
        System.out.println("Doctor First Name: " + rset.getString("first_name"));
        System.out.println("Doctor Last Name: " + rset.getString("last_name"));
        System.out.println("Doctor Gender: " + rset.getString("gender") + "(0 is Male, 1 is Female)");
      } else{
        System.out.println("Query Returned No Results.");
      }
      rset.close();
      query_statement.close();
    }
    catch (SQLException statement_error) {
      System.out.println("Query was unsuccessful.");
      System.out.println(statement_error.getMessage() + ".");
      return;
    }
  }
  
  // Query the admission table and print information
  public static void option3(Connection database, int admission_number) {
    try {
      Statement query_statement = database.createStatement();
      String query = "select admission_number, patient_ssn, admission_date, total_payment from Admission where admission_number = " + admission_number;
      ResultSet rset = query_statement.executeQuery(query);
      if(rset.next()){
        System.out.println("Admission Number: " + rset.getInt("admission_number"));
      System.out.println("Patient SSN: " + rset.getInt("patient_ssn"));
      System.out.println("Admission Date: " + rset.getDate("admission_date"));
      System.out.println("Total Payment: " + rset.getInt("total_payment"));
      } else {
        System.out.println("Admission Query Returned No Results.");
      }

      System.out.println("Rooms:");
      query = "select room_number, start_date, end_date from StayIn where admission_number = " + admission_number;
      rset = query_statement.executeQuery(query);
      while(rset.next()){
        int room_number = rset.getInt("room_number");
        Date from_date = rset.getDate("start_date");
        Date to_date = rset.getDate("end_date");
        System.out.println("Room Num: " + room_number + " FromDate: " + from_date + " ToDate: " + to_date);
      }

      System.out.println("Doctors Examined This Patient in This Admission:");
      query = "select doctor_id from Examinations where admission_number = " + admission_number;
      rset = query_statement.executeQuery(query);
      while(rset.next()){
        int doctor_id = rset.getInt("doctor_id");
        System.out.println("DoctorID: " + doctor_id);
      }
      rset.close();
      query_statement.close();
    }
    catch (SQLException statement_error) {
      System.out.println("Query was unsuccessful.");
      System.out.println(statement_error.getMessage() + ".");
      return;
    }
  }
  
  // Update the total payment value for the specified admission number
  public static void option4(Connection database, int admission_number, int new_total_payment) {
    try {
      Statement update_statement = database.createStatement();
      String update = "update Admission set total_payment = " + new_total_payment + " where admission_number = " + admission_number;
      int row_count = update_statement.executeUpdate(update);
      System.out.println("Updateted " + row_count + " rows.");
      update_statement.close();
    }
    catch (SQLException statement_error) {
      System.out.println("Update was unsuccessful.");
      System.out.println(statement_error.getMessage() + ".");
      return;
    }
  }

}
