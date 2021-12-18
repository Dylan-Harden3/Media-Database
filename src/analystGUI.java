import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// our class to create the analystGUI
public class analystGUI extends JFrame implements ActionListener {
    // J elements for different features
    JTextField txtarea;
    JTextArea mostWatchedMovies, shortestPath;
    public JRadioButton timeOne, timeTwo, timeThree, timeFour, cult_classics;
    public JButton rad_pan_button, text_area_button;

    public JFrame frame;
    
    // our constructor for analystGUI will create and display the analystGUI
    public analystGUI () {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JPanel b3=new JPanel();

        text_area_button = new JButton("Find Shortest Path");
        text_area_button.addActionListener(this);
        txtarea = new JTextField(20);
        txtarea.setText("<titleid1> <titleid2>");
        b3.add(txtarea);
        b3.add(text_area_button);

        JLabel companyName = new JLabel("Watchorama", SwingConstants.CENTER);
        mostWatchedMovies = new JTextArea("Select a Time Interval");
        mostWatchedMovies.setEditable(false);
        shortestPath = new JTextArea("Enter 2 titleids");
        shortestPath.setEditable(false);

        JTextArea classics = new JTextArea("Cult Classics");
        classics.setEditable(false);


        JPanel b1=new JPanel();
        JLabel cultLabel = new JLabel("Cult Classics: ", SwingConstants.CENTER);

        // make RadioButtons
        timeOne = new JRadioButton("2000 - 2001");
        timeTwo = new JRadioButton("2002 - 2003");
        timeThree = new JRadioButton("2004 - 2005");
        timeFour = new JRadioButton("All Time");

        // Add RadioButtons to Button Group so they work in sync
        ButtonGroup rad_button_group = new ButtonGroup();
        rad_button_group.add(timeOne);
        rad_button_group.add(timeTwo);
        rad_button_group.add(timeThree);
        rad_button_group.add(timeFour);

        // make button for bottom of RadioButton Panel
        rad_pan_button = new JButton("submit");
        rad_pan_button.addActionListener(this);

        // add to the action listener
        // add RadioButtons
        b1.add(timeOne);
        b1.add(timeTwo);
        b1.add(timeThree);
        b1.add(timeFour);
        b1.add(rad_pan_button);

        JLabel hollywoodPairs = new JLabel("HollyWood Pairs:", SwingConstants.CENTER);
        JTextArea names = new JTextArea();
        names.setEditable(false);

        // create our panel, we add the elements for the specific features at certain grid positions
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20 , 20, 20, 20);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(companyName,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(hollywoodPairs,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(names,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(b1,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(mostWatchedMovies,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(b3,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(shortestPath,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(cultLabel,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(classics,gbc);

        // get the cultClassics
        classics.setText(CultClassics.getClassics());

        // get hollywood Pairs
        names.setText(getHollyWoodPairs());
        frame.add(panel,BorderLayout.CENTER);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    // function to respond to an action
    public void actionPerformed (ActionEvent event) {
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
        String sqlStmt = "";
        Object src = event.getSource();
        String startStmt = "Most watched titles ";

        // get the time period the user selected
        if (src == rad_pan_button) {
            if (timeOne.isSelected()) {
                startStmt += "from 2000 - 2001";
                sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '1999-12-30' AND date <= '2001-12-31' GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
            }
            else if (timeTwo.isSelected()) {
                startStmt += "from 2002 - 2003";
                sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '2002-01-01' AND date <= '2003-12-31' GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
            }
            else if (timeThree.isSelected()) {
                startStmt += "from 2004 - 2005";
                sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '2004-01-01' AND date <= '2005-12-31' GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
            }
            else if (timeFour.isSelected()) {
                startStmt += "All Time";
                sqlStmt = "SELECT titleid, COUNT(titleid) AS \"most_watched\" FROM customerratings WHERE date >= '1999-12-30' AND date <= '2005-12-31' GROUP BY titleid ORDER BY \"most_watched\" DESC LIMIT 10;";
            }
            else if (cult_classics.isSelected()) {
                startStmt = "Classics";
                sqlStmt = "SELECT * FROM customerratings WHERE (rating >= 4);";
            }
            startStmt += "\n";
            try {
                // now we execute the statement we made
                Statement statement = conn.createStatement();
                ResultSet set = statement.executeQuery(sqlStmt);
                String result = "";
                while (set.next()) {
                    result += set.getString("titleid") + " " + set.getString("most_watched") + " ";
                }

                // split by space to get array of titles
                String[] titles = result.split(" ");

                // now we get the names of all of the titles
                String output = "";
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

                // now we output the names
                for (int i = 0 ; i < names.size() ; i++) {
                    if (i == names.size() - 1) {
                        output += names.get(i);
                    } else {
                        output += names.get(i) + "\n";
                    }
                }
                output = startStmt + output;
                mostWatchedMovies.setText(output);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, "Error accessing Database");
            }
        }
        else if (src == text_area_button) {
            // if the shortest path button was selected
            String s = event.getActionCommand();
            if (s.equals("Find Shortest Path")) {
                ShortestPath p = new ShortestPath();
                String[] titles = txtarea.getText().split(" ");

                // if the user did not enter two titleids separated by a space we display an error message
                if (titles.length != 2 || !hasTitleIds(titles)) {
                    JOptionPane.showMessageDialog(null, "You must enter <titleid1> <titleid2>");
                } else {
                    // now we check that the titleids they entered exist in our database
                    if (!containsTitleid(titles[0])) {
                        JOptionPane.showMessageDialog(null, "Invalid titleid: " + titles[0]);
                    } else if (!containsTitleid(titles[1])) {
                        JOptionPane.showMessageDialog(null, "Invalid titleid: " + titles[1]);
                    } else {
                        // find the shortest path
                        ArrayList<String> path = p.find_shortest_path(titles[0], titles[1]);
                        String output = "";
                        for (int i = path.size() - 1; i >= 0; i--) {
                            output += path.get(i) + " ";
                        }

                        // now we turn the titleids on the path into title names
                        String[] pathElements = output.split(" ");
                        String pathText = "";
                        for (int i = 0; i < pathElements.length; i++) {
                            String titleNameStmt = "SELECT * FROM mediainfo";
                            try {
                                Statement stmt = conn.createStatement();
                                if (pathElements[i].charAt(0) == 't') {
                                    titleNameStmt += " WHERE titleid = '" + pathElements[i] + "';";
                                    ResultSet name = stmt.executeQuery(titleNameStmt);
                                    name.next();
                                    pathElements[i] = name.getString("titlename");
                                }
                            } catch (Exception exc) {
                                JOptionPane.showMessageDialog(null, "Error accessing Database, Make sure you enter <titleid1> <titleid2>");
                            }
                        }
                        String finalText = "";

                        // now we have the vector that contains the path elements we turn it into a string and set the text
                        for (int i = 0; i < pathElements.length; i++) {
                            if (i == pathElements.length - 1) {
                                finalText += pathElements[i];
                            } else {
                                finalText += pathElements[i] + " -> ";
                            }
                        }
                        shortestPath.setText(finalText);
                    }
                }

                // reset the search area
                txtarea.setText("<titleid1> <titleid2>");
            }
        }
        try {
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // check if a list of Strings contains titleids, this is defined by the first character being 't' and the last character being a digit
    public boolean hasTitleIds (String[] list) {
        for (int i = 0 ; i < list.length ; i++) {
            if (list[i].charAt(0) != 't' || list[i].charAt(1) != 't' || !Character.isDigit(list[i].charAt(list[i].length() - 1))) {
                return false;
            }
        }
        return true;
    }

    // check if a titleid is present in our customerratings table and mediainfo table
    public boolean containsTitleid (String titleid) {
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
            // if the result returns nothing then titleid is not present and we return false
            Statement stmt = conn.createStatement();
            String checkTitles = "SELECT * FROM customerratings WHERE titleid = '" + titleid + "';";
            ResultSet list = stmt.executeQuery(checkTitles);
            String titleids = "";

            while (list.next()) {
                titleids += list.getString("titleid");
            }

            if (titleids == "") {
                try {
                    conn.close();
                } catch(Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
                return false;
            }
            checkTitles = "SELECT * FROM mediainfo WHERE titleid = '" + titleid + "';";
            list = stmt.executeQuery(checkTitles);
            titleids = "";

            while (list.next()) {
                titleids += list.getString("titleid");
            }
            if (titleids == "") {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
                return false;
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Error accessing database in containsTitleId");
        }

        try {
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return true;
    }

    public static String getHollyWoodPairs () {
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
        String output = "";

        try {
            String sqlStmt = "SELECT AVG(m.avgrating) AS tavg, t1.personid as p1, t2.personid as p2 FROM peoplemedia t1 INNER JOIN peoplemedia t2 ON t1.titleid = t2.titleid INNER JOIN mediaratings m ON t1.titleid = m.titleid WHERE t1.personid != t2.personid and t1.job != 'director' and t2.job != 'director' GROUP BY t1.personid, t2.personid ORDER BY tavg DESC LIMIT 20;";
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(sqlStmt);
            Vector<Double> ratings = new Vector<>();
            HashMap<String,String> pairs = new HashMap<>();
            while (set.next()) {
                String firstActor = set.getString("p1");
                String secondActor = set.getString("p2");
                String rating = set.getString("tavg");
                boolean isDuplicate = false;
                if (pairs.get(secondActor) != null) {
                    if (pairs.get(secondActor).equals(firstActor)) {
                        isDuplicate = true;
                    }
                }
                if (!isDuplicate) {
                    pairs.put(firstActor, secondActor);
                    ratings.add(Double.parseDouble(rating));
                }
            }

            int counter = 0;
            for (Map.Entry<String,String> pair : pairs.entrySet()) {
                String actorOne = "";
                String actorTwo = "";
                sqlStmt = "SELECT * FROM peoplenames WHERE personid = '" + pair.getKey() + "';";
                set = statement.executeQuery(sqlStmt);
                while (set.next()) {
                    actorOne = set.getString("name");
                }
                sqlStmt = "SELECT * FROM peoplenames WHERE personid = '" + pair.getValue() + "';";
                set = statement.executeQuery(sqlStmt);
                while (set.next()) {
                    actorTwo= set.getString("name");
                }
                if (counter == pairs.size() - 1) {
                    output += actorOne + " & " + actorTwo + " " + ratings.get(counter);
                } else {
                    output += actorOne + " & " + actorTwo + " " + ratings.get(counter) + "\n";
                }
                counter++;
            }
        } catch(Exception e){
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
        return output;
    }
}