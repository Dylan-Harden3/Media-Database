import java.sql.*;
import java.util.*;
import java.util.Vector;

public class HollywoodPairs {

    public static void getTitles() {
        Connection conn = null;
        String teamNumber = "1";
        String sectionNumber = "901";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "901Team1";
        try {
            conn = DriverManager.getConnection(dbConnectionString, userName, userPassword);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println(exc.getClass().getName() + ": " + exc.getMessage());
            System.exit(0);
        }
        try {
            Statement stmt = conn.createStatement();
            String sqlStmt = "select distinct titleid from peoplemedia;";
            ResultSet set = stmt.executeQuery(sqlStmt);
            Vector<String> titleids = new Vector<>();
            while(set.next()){
                titleids.add(set.getString("titleid"));
            }

            Set<Vector<String>> pairs = new HashSet<Vector<String>>();
            for(int i = 0 ; i < titleids.size() ; i++){
                if(i % 1000 == 0){
                    System.out.println(i);
                }
                sqlStmt = "select * from peoplemedia where titleid = '" + titleids.get(i) + "' and job != 'director';";
                set = stmt.executeQuery(sqlStmt);
                Vector<String> curPeople = new Vector<>();
                while(set.next()){
                    curPeople.add(set.getString("personid"));
                }
                for(int j = 0 ; j < curPeople.size() ; j++){
                    for(int k = 0 ; k < curPeople.size() ; k++){
                        Vector<String> curPair = new Vector<String>();
                        curPair.add(curPeople.get(j));
                        curPair.add(curPeople.get(k));
                        if(!containsPair(pairs,curPair)){
                            pairs.add(curPair);
                        }
                    }
                }
            }
            System.out.println(pairs.size());
        } catch (Exception e) {

        }
    }
    public static boolean containsPair(Set<Vector<String>> pairs, Vector<String> curPair){
        for(Vector<String> pair : pairs){
            if(pair.get(0) == curPair.get(0) && pair.get(1) == curPair.get(1)){
                return true;
            }else if(pair.get(0) == curPair.get(1) && pair.get(1) == curPair.get(0)){
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) throws Exception {
        getTitles();
    }
}
