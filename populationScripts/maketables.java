package populationScripts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class maketables {

    //creates our tables
    static void createTables() {
        Connection conn = null;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315901_1db";
        String userName = "csce315901_1user";
        String userPassword = "901Team1";
        try {
            conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");
        
        try{
            Statement stmt = conn.createStatement();
            //System.out.println("--------------------Query Results--------------------");
            String createPeopleNames = "CREATE TABLE peoplenames (personid TEXT PRIMARY KEY, name TEXT);";
            String createPeopleMedia = "CREATE TABLE peoplemedia (personid TEXT, job TEXT, titleid TEXT);";
            String createCustomerRatings = "CREATE TABLE customerratings (customerid INT, rating int, date date, titleid TEXT);";
            String createMediaRatings = "CREATE TABLE mediaratings (titleid TEXT PRIMARY KEY, year INT, avgrating FLOAT, numvotes INT, genre TEXT);";
            String createMediaInfo = "CREATE TABLE mediainfo (titleid TEXT PRIMARY KEY, type TEXT, titlename TEXT, genre TEXT, runtime INT);";
            stmt.executeUpdate(createPeopleNames);
            stmt.executeUpdate(createPeopleMedia);
            stmt.executeUpdate(createCustomerRatings);
            stmt.executeUpdate(createMediaRatings);
            stmt.executeUpdate(createMediaInfo);
            System.out.println("Tables Created Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            conn.close();
            System.out.println("Connection to Create Tables Closed.");
        } catch(Exception e) {
            System.out.println("Connection to Create Tables NOT Closed.");
        }
    }
    public static void main(String[] args) throws Exception  {  
        //make the tables
        createTables();
    }
}