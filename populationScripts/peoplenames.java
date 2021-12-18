package populationScripts;
import java.io.*;
import java.util.*;
import java.sql.*;

public class peoplenames {
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
        File f = new File("../data/namesClean.csv");
        Scanner sc = new Scanner(f,"UTF-8");

        while(sc.hasNextLine()) {

            String personid = "";
            String name = "";

            line = sc.nextLine();
            StringTokenizer tokens = new StringTokenizer(line, ",");

            personid = tokens.nextToken();
            name = tokens.nextToken();
            
            name = name.replace("'", "''");
            String statement = "INSERT INTO peoplenames VALUES('" + personid + "','" + name + "');";
            
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