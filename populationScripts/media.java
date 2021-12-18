package populationScripts;
import java.io.File;
import java.sql.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class media {
    
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
            File file = new File("../data/titlesClean.csv");

            Scanner scan = new Scanner(file,"UTF-8");
            
            while(scan.hasNextLine()) {

                String line = scan.nextLine();

                String TitleID = "";
                String TitleType = "";
                String OrigTitle = "";
                String Genres = "";
                String Runtime = "";
                String NumVotes = "";
                String Year = "";
                String AvgRating = "";

                //extract elements and store in temp variables
                StringTokenizer tokens = new StringTokenizer(line, "\t");
                
                while(tokens.hasMoreTokens()) {
                    TitleID = tokens.nextToken();
                    //System.out.println(TitleID);
                    TitleType = tokens.nextToken();
                    //System.out.println(TitleType);
                    OrigTitle = tokens.nextToken();
                    OrigTitle = OrigTitle.replace('\'', '`');

                    //System.out.println(OrigTitle);
                    Runtime = tokens.nextToken();
                    //System.out.println(Runtime);
                    Genres = tokens.nextToken();
                    //System.out.println(Genres);
                    Year = tokens.nextToken();
                    //System.out.println(Year);
                    AvgRating = tokens.nextToken();
                    //System.out.println(AvgRating);
                    NumVotes = tokens.nextToken();
                    //System.out.println(NumVotes);
                    //System.out.println();
                }

                //create statements
                String sqlStatement = "INSERT INTO mediaratings VALUES('" + TitleID + "', " + Year + ", " + AvgRating + ", " + NumVotes + ", '" + Genres + "');";
                String sqlStatement2 = "INSERT INTO mediainfo VALUES('" + TitleID + "', '" + TitleType + "', '" + OrigTitle + "', '" + Genres + "', " + Runtime + ");";

                //insert into table
                stmt.executeUpdate(sqlStatement);
                stmt.executeUpdate(sqlStatement2);
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