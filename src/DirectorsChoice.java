import java.sql.*;
import java.util.*;
// import java.util.Scanner;
// import java.io.File;

/*
 * CSCE 315 - Project 2
 * Phase 3 Directors Choice Algo
 * 
 * MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
 * 
 * to run script: 
 * compile: javac DirectorsChoice.java
 * run: java -cp '.;postgresql-42.2.8.jar' DirectorsChoice
 */

class DirectorsRec {
    public String title;
    public String director;
    public boolean fromDirector;

    DirectorsRec (String t, String d, boolean f) {
        title = t;
        director = d;
        fromDirector = f;
    }
}

public class DirectorsChoice {

    // to hold total ratings and rating total
    private static class Ratings {
        private float rating;
        private int count;

        Ratings () {
            rating = 0;
            count = 0;
        }

        // adds rating to set
        public void addRating (float r) {
            rating += r;
            count++;
        }

        // gets average of ratings for item
        public float getAverage () {
            return rating / (float) count;
        }
    }

    public static DirectorsRec getRec (int userID) {
        // to connect to database
        Connection conn = null;
        String teamNumber = "1";
        String sectionNumber = "901";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "901Team1";

        String recTitle = "";
        String recDirector = "";
        boolean recMade = false;

        // Connecting to the database 
        try {
            conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");

        // hash map to store type of genre with number of total reviews and total rating
        HashMap<String, Ratings> genres = new HashMap<>();

        // SET UP MESSAGE FOR FAV GENRE
        String message = "SELECT mediainfo.genre, customerratings.rating FROM mediainfo, customerratings WHERE mediainfo.titleid = customerratings.titleid AND customerratings.customerid = " + userID + ";";
            
        // sending message to database
        try {
            Statement stmt = conn.createStatement();
            ResultSet genreQuery = stmt.executeQuery(message);

            // add genre, rating info to hash map
            while (genreQuery.next()) {
                // returns string under "genre" and int under "rating"
                String[] ratingGenre = genreQuery.getString("genre").split(",");

                // rating to add to each genre
                float rating = Float.parseFloat(genreQuery.getString("rating"));

                // adding rating to each genre
                for (int i = 0; i < ratingGenre.length; ++i) {
                    if (genres.get(ratingGenre[i]) == null) {
                        // need to add genre
                        genres.put(ratingGenre[i], new Ratings());
                    } 
                    genres.get(ratingGenre[i]).addRating(rating);
                }
            }
            genreQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        // finding fav genre (highest average)
        String favGenre = "";
        float favRating = 0;

        for (Map.Entry<String, DirectorsChoice.Ratings> e : genres.entrySet()) {
            if (e.getValue().getAverage() > favRating) {
                favGenre = e.getKey();
                favRating = e.getValue().getAverage();
            }
        }

        // getting fav director
        message = "SELECT peoplemedia.personid, AVG(customerratings.rating) AS \"avg\" FROM customerratings INNER JOIN peoplemedia ON peoplemedia.titleid = customerratings.titleid WHERE peoplemedia.job = 'director' AND customerratings.customerid = " + userID + " GROUP BY peoplemedia.personid ORDER BY \"avg\" DESC;";

        try {
            // creating second connection for movie queries (need to have multiple result for multiple queries)
            Connection movieConn1 = null;

            try {
                movieConn1 = DriverManager.getConnection(dbConnectionString,userName, userPassword);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
            Statement movieStmt1 = movieConn1.createStatement();
            System.out.println("Opened database successfully: movieConn1");

            // creating third connection for movie queries (need to have multiple result for multiple queries)
            Connection movieConn2 = null;

            try {
                movieConn2 = DriverManager.getConnection(dbConnectionString,userName, userPassword);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
            Statement movieStmt2 = movieConn2.createStatement();
            System.out.println("Opened database successfully: movieConn2");

            Statement stmt = conn.createStatement();
            ResultSet directorQuery = stmt.executeQuery(message);
    
            // from each director, search for most popular movie from fav genre
            // if one is found, return it
            while (directorQuery.next()) {
                // getting directors id to search system for
                String favDirector = directorQuery.getString("personid");

                // getting movie reccommendation based on director, genre
                message = "SELECT mediaratings.titleid FROM mediaratings INNER JOIN customerratings ON mediaratings.titleid = customerratings.titleid INNER JOIN peoplemedia ON mediaratings.titleid = peoplemedia.titleid WHERE mediaratings.genre LIKE '%" + favGenre + "%' AND peoplemedia.personid = '" + favDirector + "' GROUP BY mediaratings.titleid, mediaratings.avgrating, peoplemedia.personid ORDER BY mediaratings.avgrating DESC;";
                // sending request for movies to server
                ResultSet movieQuery = movieStmt1.executeQuery(message);

                // telling user movie rec if there are any movies they have not seen
                while (movieQuery.next()) {
                    String title = movieQuery.getString("titleid");
                    message = "SELECT titleid FROM customerratings WHERE customerid = " + userID + " AND titleid = '" + title + "';";

                    try {
                        movieStmt2.executeQuery(message);
                    } catch(Exception e) {
                        // haven't seen movie
                        recMade = true;
                        recTitle = title;
                        recDirector = favDirector;
                    }           
                }
            }

            // if rec has not yet been made, find most popular in favorite genre
            if (!recMade) {
                System.out.println("You have seen all the movies in your favorite genre by your favorite director");
                
                // finding most popular movie from fav genre user hasn't seen
                message = "SELECT mediaratings.titleid, peoplenames.name FROM mediaratings INNER JOIN peoplemedia ON peoplemedia.titleid = mediaratings.titleid INNER JOIN peoplenames ON peoplenames.personid = peoplemedia.personid WHERE mediaratings.genre LIKE '%" + favGenre + "%' GROUP BY mediaratings.titleid, peoplenames.name ORDER BY mediaratings.avgrating DESC;";
                ResultSet movieQuery = movieStmt1.executeQuery(message);

                while (movieQuery.next()) {
                    String title = movieQuery.getString("titleid");
                    message = "SELECT titleid FROM customerratings WHERE customerid = " + userID + " AND titleid = '" + title + "'';";

                    try {
                        movieStmt2.executeQuery(message);
                    } catch (Exception e) {
                        // haven't seen movie
                        recTitle = title;
                        recDirector = movieQuery.getString("name");
                        break;
                    }
                }
                movieQuery.close();
            }

            // closing the connection
            try {
                movieConn1.close();
                System.out.println("Connection (movieConn1) Closed.");
            } catch(Exception e) {
                System.out.println("Connection NOT Closed.");
            } 

            // closing the connection
            try {
                movieConn2.close();
                System.out.println("Connection (movieConn2) Closed.");
            } catch(Exception e) {
                System.out.println("Connection NOT Closed.");
            } 
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        // getting title from database
        message = "SELECT titlename FROM mediainfo WHERE titleid = '" + recTitle + "' LIMIT 1;";
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet title = stmt.executeQuery(message);
            title.next();
            recTitle = title.getString("titlename");
        } catch (Exception e) {
            System.out.println("name of title not in database, returning title id: " + recTitle);
        }
        
        // closing the connection
        try {
            conn.close();
            System.out.println("Connection (conn) Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        } 

        DirectorsRec r = new DirectorsRec(recTitle, recDirector, recMade);
        return r;
    }   
}