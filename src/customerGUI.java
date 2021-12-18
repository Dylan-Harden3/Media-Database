import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// class for customerGUI, the GUI is a JFrame
public class customerGUI extends JFrame implements ActionListener {

    // here we have members for specific elements on the GUI which require functions to be implemented
    int customerid;

    // buttons to select from specific time periods
    public JRadioButton timeOne, timeTwo, timeThree, timeFour;
    public JButton displayWatchHistory;

    // frame to display the GUI,
    public JFrame frame;
    public JTextArea watchHistory;

    // our constructor for customerGUI will create and display the customerGUI
    public customerGUI (int id) throws Exception {
        customerid = id;
        frame =  new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        JLabel cName = new JLabel("Watchorama", SwingConstants.CENTER);
        JLabel filterTime = new JLabel("Filter By Time Perioid", SwingConstants.CENTER);
        JLabel name = new JLabel("Title name", SwingConstants.CENTER);
        JLabel directorsChoice = new JLabel("Directors Choice: ", SwingConstants.CENTER);

        JTextArea directorsChoiceText = new JTextArea("Directors Choice Here");
        JTextArea custom = new JTextArea("Customer Recomendations here");
        custom.setEditable(false);

        watchHistory = new JTextArea("Select time interval");
        watchHistory.setEditable(false);
        JPanel b1=new JPanel();

        // make RadioButtons
        timeOne = new JRadioButton("2000 - 2001");
        timeTwo = new JRadioButton("2002 - 2003");
        timeThree = new JRadioButton("2004 - 2005");
        timeFour = new JRadioButton("All Time");

        // Add RadioButtons to Button Group so they work in sync
        ButtonGroup timeButtons = new ButtonGroup();
        timeButtons.add(timeOne);
        timeButtons.add(timeTwo);
        timeButtons.add(timeThree);
        timeButtons.add(timeFour);

        // make button for bottom of RadioButton Panel
        displayWatchHistory = new JButton("Display Watch History");
        displayWatchHistory.addActionListener(this);

        // add RadioButtons
        b1.add(timeOne);
        b1.add(timeTwo);
        b1.add(timeThree);
        b1.add(timeFour);
        b1.add(displayWatchHistory);

        // create our panel, we add the elements for the specific features at certain grid positions
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(cName,gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(directorsChoice,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(directorsChoiceText,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(custom,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(filterTime,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(name,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(b1,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(watchHistory,gbc);

        Connection conn = null;
        String teamNumber = "1";
        String sectionNumber = "901";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "901Team1";

        // now we edit the elements with the data specific to the customerid which the user logged in with
        // Connecting to the database 
        try {
            conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println(exc.getClass().getName() + ": " + exc.getMessage());
            System.exit(0);
        }

        // Customer recommendations section, first we get the customers top 10 most watched titles
        String sqlstmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE customerid = " + customerid + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        Statement statement = conn.createStatement();
        ResultSet set = statement.executeQuery(sqlstmt);
        String result = "";

        // we add the titles to a string separating by /
        while(set.next()) {
            result += set.getString("titleid") + "/";
        }

        // add the titles to an array using split
        String[] titleids = result.split("/");
        result = "";

        // iterate through the top 10 titles, collecting the genre for ach title
        for (int i = 0 ; i < titleids.length ; i++) {
            sqlstmt = "SELECT * FROM mediainfo WHERE titleid = '" + titleids[i] + "';";
            set = statement.executeQuery(sqlstmt);
            while(set.next()) {
                result += set.getString("genre") + "/";
            }
        }

        // now we separate each titles genre(s) into an array
        String[] titlegenres = result.split("/");

        // make a hashmap where the keys are Strings which represent genres and the values are Integers which represent how many times a genre has occured
        HashMap<String,Integer> genreOccs = new HashMap<>();
        for (int i = 0 ; i < titlegenres.length ; i++) {

            // for each titles genres, we split by "," to get all the genres
            String[] curGenres = titlegenres[i].split(",");
            for (int j = 0 ; j < curGenres.length ; j++) {

                // iterate the current titles genres, if the genre is present in the hashmap we increment the value, if not we add a new key with value 1
                if (genreOccs.containsKey(curGenres[j])) {
                    genreOccs.put(curGenres[j], genreOccs.get(curGenres[j]) + 1);
                } else {
                    genreOccs.put(curGenres[j], 1);
                }
            }
        }

        // make a vector to store the top three genres
        Vector<String> genres = new Vector<>();
        for (int i = 0 ; i < 3 ; i++) {

            // iterate three times to get the top three
            int maxOcc = 0;
            String maxKey = "";

            // each time we find the key (genre) which has occured the most
            for (Map.Entry<String, Integer> e : genreOccs.entrySet()) {
                if (e.getValue() > maxOcc) {
                    maxKey = e.getKey();
                    maxOcc = e.getValue();
                }
            }

            // now we add the most common genre to the array and remove it from the hashmap
            genreOccs.remove(maxKey);
            genres.add(maxKey);
        }
        result = "";
        
        // iterate through the array of top three genres, getting the top 3/4 rated titles in the genre
        for (int i = 0 ; i < genres.size() ; i++) {

            // start the sql statement
            sqlstmt = "SELECT titleid, numvotes, avgrating FROM mediaratings WHERE genre LIKE '%";
            sqlstmt += genres.get(i) + "%'";

            // since we have three genres and we need the top 10 titles, we have the first genre have 4 titles
            if (i == 0) {
                sqlstmt += "and avgrating > 7.5 order by numvotes desc limit 4;";
            } else {
                sqlstmt += "and avgrating > 7.5 order by numvotes desc limit 3;";
            }

            // get the titleids of the top 3/4 titles for the genre
            set = statement.executeQuery(sqlstmt);
            while (set.next()) {
                result += set.getString("titleid") + "\n";
            }
        }

        // we have a list of the top 10 titleids, now we find the name of them
        String[] titles = result.split("\n");
        result = "";
        int curTitle = 0;

        // iterate through the list of titleids, each time getting the name corresponding to the current titleid
        for (int i = 0 ; i < titles.length ; i++) {
            sqlstmt = "select * from mediainfo where titleid = '" + titles[i] + "';";
            set = statement.executeQuery(sqlstmt);
            while (set.next()) {
                //if we are on the 0th, 4th, or 7th title we add a header of the genre
                if (i == 0 || i == 4 || i == 7) {
                    result += genres.get(curTitle) + "- \n";
                    curTitle++;
                }
                if (i == titles.length - 1) {
                    result += set.getString("titlename");
                } else {
                    result += set.getString("titlename") + "\n";
                }
            }

        }

        // now we have the 10 recommendations so we set the textarea to display them
        result = "Your Recommendations: \n" + result;
        custom.setText(result);

        // get the directors recommendations
        DirectorsRec rec = DirectorsChoice.getRec(id);
        directorsChoiceText.setText(rec.title + " Directed by: " + rec.director);

        if (!rec.fromDirector) {
            directorsChoiceText.setText("You have already seen all movies by your favorite director(s): " + directorsChoiceText.getText());
        }

        // make the frame visible
        frame.add(panel,BorderLayout.CENTER);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        
        try {
            conn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println(exc.getClass().getName() + ": " + exc.getMessage());
            System.exit(0);
        }
    }

    // our function to update the text area for the users watch history from a time period
    public void actionPerformed(ActionEvent e) {

        Connection conn = null;
        String teamNumber = "1";
        String sectionNumber = "901";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "901Team1";

        // Connecting to the database 
        try {
            conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println(exc.getClass().getName() + ": " + exc.getMessage());
            System.exit(0);
        }

        String sqlStmt = "";
        String startStmt = "Your watch history ";

        // create an sql statement to retrieve the watch history from the time period that was selected
        if (timeOne.isSelected()) {
            startStmt += "from 2000 - 2001";
            sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '1999-12-30' AND date <= '2001-12-31' AND customerid = " + customerid + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        }
        else if (timeTwo.isSelected()) {
            startStmt += "from 2002 - 2003";
            sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '2002-01-01' AND date <= '2003-12-31' AND customerid = " + customerid + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        }
        else if (timeThree.isSelected()) {
            startStmt = "from 2004 - 2005";
            sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '2004-01-01' AND date <= '2005-12-31' AND customerid = " + customerid + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        }
        else if (timeFour.isSelected()) {
            startStmt += "All Time";
            sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '1999-12-30' AND date <= '2005-12-31' AND customerid = " + customerid + " GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
        }
        startStmt += "\n";

        // now we execute the statement
        try {
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(sqlStmt);
            String result = "";

            // retrieve the titleids from the time period
            while (set.next()) {
                result += set.getString("titleid")+ "\n";
            }
            String output = "";

            // if there is no output, then this means there was no data from the selected time interval
            if (result.length() == 0) {
                output = "No watch history data from selected time interval";
            } else {
                // now we get the names from the list of titleids
                String[] titles = result.split("\n");
                Vector<String> names = new Vector<>();
                for (String s : titles) {
                    if (s.charAt(0) == 't') {
                        sqlStmt = "SELECT titlename FROM mediainfo WHERE titleid = '" + s + "';";
                        set = statement.executeQuery(sqlStmt);
                        while (set.next()) {
                            names.add(set.getString("titlename"));
                        }
                    }
                }

                // add the names to the output
                for (int i = 0 ; i < names.size() ; i++) {
                    if (i == names.size() - 1) {
                        output += names.get(i);
                    } else {
                        output += names.get(i) + "\n";
                    }
                }
            }
            output = startStmt + output;
            watchHistory.setText(output);
        } catch(Exception exc) {
            JOptionPane.showMessageDialog(null,"Error accessing Database");
        }

        try {
            conn.close();
        } catch(Exception exc) {
        }
    }
}