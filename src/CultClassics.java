import java.util.*;
import java.sql.*;

// class containing the methods to get the CultClassics
public class CultClassics {

    public static String getClassics() {
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

        try {
            // first we select all ratings in customerratings where the rating is 4 or 5
            Statement statement = conn.createStatement();
            String sqlStmt = "SELECT * FROM customerratings WHERE rating > 3;";
            ResultSet set = statement.executeQuery(sqlStmt);

            // create a hashmap to store a titleid along with the amount of times it has a rating of 4 or 5
            HashMap<String,Integer> titles = new HashMap<String, Integer>();
            String id = "";

            // now iterate through all the ratings, adding to the hashmap or incrementing the value
            while (set.next()) {
                id = set.getString("titleid");
                if (titles.containsKey(id)) {
                    titles.put(id, titles.get(id) + 1);
                } else {
                    titles.put(id, 1);
                }
            }

            // now we iterate through the hashmap 10 times, each time storing the title with the most views
            Vector<String> topTen = new Vector<>();
            Vector<Integer> topTenNumViews = new Vector<>();

            for (int i = 0 ; i < 10 ; i++) {
                String maxKey = "";
                Integer maxValue = 0;
                for (Map.Entry<String,Integer> entry : titles.entrySet()) {
                    if (entry.getValue() > maxValue) {
                        maxValue = entry.getValue();
                        maxKey = entry.getKey();
                    }
                }
                topTen.add(maxKey);
                topTenNumViews.add(maxValue);
                titles.remove(maxKey);
            }

            // now we turn the top 10 titleids into the top 10 names
            Vector<String> topTenNames = new Vector<>();
            
            for (int i = 0 ; i < topTen.size() ; i++) {
                sqlStmt = "SELECT * FROM mediainfo WHERE titleid = '" + topTen.get(i) + "';";
                set = statement.executeQuery(sqlStmt);
                while (set.next()) {
                    topTenNames.add(set.getString("titlename"));
                }
            }
            String output = "";

            // Finally, iterate through the names and add the name + numViews to the output
            for (int i = 0 ; i < topTenNames.size() ; i++) {
                if (i == topTenNames.size() - 1) {
                    output += topTenNames.get(i) + " " + topTenNumViews.get(i);
                } else {
                    output += topTenNames.get(i) + " " + topTenNumViews.get(i) + "\n";
                }
            }
            return output;
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        try {
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return "error retrieving CultClassics";
    }
}
