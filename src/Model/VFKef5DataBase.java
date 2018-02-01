package Model;

import java.sql.*;


public class VFKef5DataBase {

    private String username;
    private String password;
    private String dbURL;

    public double getKef5Price(String query) throws SQLException {
        double answer = -1 ;
        //read credentials for kef5 database from xml file
        XMLReader db =new XMLReader();
        XMLReader.DatabaseEntry dbCredentials =db.getDatabaseCredentials();

        username = dbCredentials.username;
        password = dbCredentials.password;
        dbURL    = dbCredentials.dbURL;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, username, password);
            if (conn != null) {
                Statement st = conn.createStatement();
                ResultSet res = st.executeQuery(query);
                while (res.next()) {
                    answer = Double.parseDouble(res.getString(1));
                }
                conn.commit();
            } else {
                System.out.println("conn is null");
            }


        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return answer;

    }


    public void updateKef5Price(String query) throws SQLException{
        //read credentials for kef5 database from xml file
        XMLReader db =new XMLReader();
        XMLReader.DatabaseEntry dbCredentials =db.getDatabaseCredentials();

        username = dbCredentials.username;
        password = dbCredentials.password;
        dbURL    = dbCredentials.dbURL;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, username, password);
            if (conn != null) {
                Statement st = conn.createStatement();

               st.executeUpdate(query);
               conn.commit();
            } else {
                System.out.println("conn is null");
            }


        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
