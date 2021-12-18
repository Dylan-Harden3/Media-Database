
import javax.swing.*;
import java.sql.*;
import java.util.regex.Pattern;

public class Login {

    public static void main (String[] args) {

        Connection conn = null;
        String teamNumber = "1";
        String sectionNumber = "901";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "901Team1";

        // Connecting to the database 
        try {
            conn = DriverManager.getConnection(dbConnectionString, userName, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        boolean loggedIn = false;

        // Display the login prompt until the user has chosen which GUI to use
        while (!loggedIn) {
            String input = JOptionPane.showInputDialog(null, "Enter \"analyst\" for analyst login or customerid for user login");
            String message = "SELECT customerid FROM customerratings WHERE (customerid = " + input + ") LIMIT 1";

            // if they have only entered leters, we check if their input is "analyst" if not we display an error message and collect input again
            if (Pattern.matches("[a-zA-Z]+", input)) {
                if (input.equalsIgnoreCase("analyst")) {
                    loggedIn = true;

                    // if they chose "analyst" we create an analystGUI which will display automatically
                    new analystGUI();
                } else {
                    // error message
                    JOptionPane.showMessageDialog(null,"You must enter either \"analyst\" or a customerid");
                }
            }else if (input.matches("[0-9]+")) {
                // if the input is all digits, we assume they are trying to access the customerGUI
                try{
                    // check if the customerid is in the customerratings table
                    Statement stmt = conn.createStatement();
                    ResultSet query = stmt.executeQuery(message);

                    if (!query.next()) {
                        // no user with the id
                        JOptionPane.showMessageDialog(null,"Invalid customerid");
                    } else {
                        // make a customerGUI with the specific customerid
                        loggedIn = true;
                        customerGUI g = new customerGUI(Integer.parseInt(input));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
            } else {
                // if the user enters a combination of letters and numbers just display an error message
                JOptionPane.showMessageDialog(null,"You must enter either \"analyst\" or a customerid");
            }
        }

        // closing the connection
        try {
            conn.close();
        } catch(Exception e) {
        }
    }
}