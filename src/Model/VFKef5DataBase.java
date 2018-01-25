package Model;

import java.sql.*;


public class VFKef5DataBase {

    String username;
    String password;
    String dbURL;

    public String getKef5Price(String query) {
        String answer = new String() ;
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
//                ResultSet res = st.executeQuery("select count(sFileId) from dbo.SMAST;");
                ResultSet res = st.executeQuery(query);
                while (res.next()) {
                    System.out.println(res.getString(1));
                    answer = res.getString(1);
                }
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();

                System.out.println("Product version: " + dm.getDatabaseProductVersion());

                conn.commit();
            } else {
                System.out.println("conn is null");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
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
}
