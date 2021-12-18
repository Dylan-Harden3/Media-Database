package populationScripts;
import java.util.*;
import java.io.*;
import java.sql.*;
public class customerratings {
    public static void main(String[] args) throws Exception {

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
        
        try {

            Statement stmt = conn.createStatement();

            String line;
            File f = new File("../data/customer_ratingsClean.csv");
            Scanner sc = new Scanner(f,"UTF-8");

            while(sc.hasNextLine()) {

                String customerid = "";
                String rating = "";
                String date = "";
                String titleid = "";

                line = sc.nextLine();
                StringTokenizer tokens = new StringTokenizer(line);
                customerid = tokens.nextToken();
                rating = tokens.nextToken();
                date = tokens.nextToken();
                titleid = tokens.nextToken();

                String statement = "INSERT INTO customerratings VALUES('" + customerid + "'," + rating + "," + "'" + date + "'," + "'" + titleid + "');";

                stmt.executeUpdate(statement);
            }
        sc.close();

    } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }
    
        try {
        conn.close();
        System.out.println("Connection Closed.");
        } catch(Exception e) {
        System.out.println("Connection NOT Closed.");
        }
    }     
}