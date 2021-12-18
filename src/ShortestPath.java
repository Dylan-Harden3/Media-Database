import java.util.*;
import java.sql.*;

class Node {
    String value;
    Node parent;

    Node (String val, Node par) {
        value = val;
        parent = par;
    }
}

public class ShortestPath {

    // used to check if the value element of a node is a movie ID or a customer ID
    public boolean is_numeric(Node item) {
        try {
            Integer.parseInt(item.value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    // finds the shortest path using breadth first search
    public ArrayList<Node> solve (String start, String end) {
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

        Node start_movie = new Node(start, null);
        ArrayList<Node> visited = new ArrayList<Node>();
        Queue<Node> next_queue = new LinkedList<Node>();

        next_queue.add(start_movie);
        visited.add(start_movie);

        while (!next_queue.isEmpty()) {

            Node next_value = next_queue.remove();

            List<Node> neighbors = new ArrayList<Node>();
            String sqlstmt = "";

            if(is_numeric (next_value)) {
                // Fill neighbors with Movies
                try {
                    Statement stmt = conn.createStatement();
                    sqlstmt = "SELECT * FROM CUSTOMERRATINGS WHERE customerid = '" + Integer.parseInt(next_value.value) + "' AND rating > 3;";
                    ResultSet set = stmt.executeQuery(sqlstmt);
                    while (set.next()) {
                        neighbors.add(new Node(set.getString("titleid"), next_value));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
            }
            else {
                // Fill neighbors with customers who rated 4 or 5 stars
                try {
                    Statement stmt = conn.createStatement();
                    sqlstmt = "SELECT * FROM CUSTOMERRATINGS WHERE titleid = '" + next_value.value + "' AND rating > 3;";
                    ResultSet set = stmt.executeQuery(sqlstmt);
                    while(set.next()){
                        neighbors.add(new Node(set.getString("customerid"), next_value));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
            }

            for (int i = 0; i < neighbors.size(); i++) {
                if (!visited.contains(neighbors.get(i))) {
                    next_queue.add(neighbors.get(i));
                    visited.add(neighbors.get(i));

                    if (neighbors.get(i).value.equals(end)) {
                        return visited;
                    }
                }
            }
        }
        return visited;
    }

    // one function to return the path starting from the end and iterating through parent nodes
    public ArrayList<String> return_path (String start, String end, List<Node> paths) {
        ArrayList<String> end_to_start_path = new ArrayList<String>();
        Node iterator = paths.get(paths.size() - 1);

        while (iterator.parent != null) {
            end_to_start_path.add(iterator.value);
            iterator = iterator.parent;
        }
        end_to_start_path.add(iterator.value);
        return end_to_start_path;
    }

    // Runs BFS and returns the shortest path
    public ArrayList<String> find_shortest_path (String start_movie, String end_movie) {

        ArrayList<Node> paths = solve(start_movie, end_movie);

        return return_path(start_movie, end_movie, paths);
    }
}

// sample test cases for ShortestPath
/*
find_shortest_path("tt8206328","tt0110116");
find_shortest_path("tt8206328","tt0441889");
find_shortest_path("tt8474620","tt0441889");
*/