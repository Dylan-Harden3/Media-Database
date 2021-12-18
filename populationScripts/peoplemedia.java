package populationScripts;
import java.io.File;
import java.sql.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class peoplemedia {

    public static void main(String args[]) throws Exception {

        //Building the connection with your credentials
        Connection conn = null;
        String teamNumber = "1";
        String sectionNumber = "901";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "901Team1";
  
        //Connecting to the database 
        try {
            conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");
       
        try {
            //create a statement object
            Statement stmt = conn.createStatement();

            //open file to read from
            File file = new File("../data/principalsClean.csv");

            Scanner scan = new Scanner(file);
            
            while(scan.hasNextLine()) {

                String line = scan.nextLine();

                String PersonID = "";
                String Job = "";
                String TitleID = "";

                //extract elements and store in temp variables
                StringTokenizer tokens = new StringTokenizer(line);
                
                while(tokens.hasMoreTokens()) {
                    TitleID = tokens.nextToken();
                    PersonID = tokens.nextToken();
                    Job = tokens.nextToken();
                }

                //create statement
                String sqlStatement = "INSERT INTO PeopleMedia VALUES('" + PersonID + "', '" + Job + "', '" + TitleID + "');";
                //System.out.println(sqlStatement);

                //insert into table
                stmt.executeUpdate(sqlStatement);
            }

            //close scanner
            scan.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        //closing the connection
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }
}