package com.heartsun.shivanagarkp.utils.connectionUtils;

import android.os.StrictMode;
import android.util.Log;
import com.heartsun.shivanagarkp.data.Prefs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SqlServerFunctions {
    public Connection ConnectToSQLServer(Prefs prefs) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try {
            String ConnectionString = "jdbc:jtds:sqlserver://"+prefs.getServerIp()+":"+prefs.getServerPort()+"/"+prefs.getDatabaseName()+";encrypt=fasle;user="+prefs.getDatabaseUser()+";password="+prefs.getDatabasePassword();
//                        String ConnectionString = "jdbc:jtds:sqlserver://192.168.0.105:2022/"+"DropcareTrial"+";encrypt=fasle;user=rajesh;password=1234560";

            Log.i("Android", ConnectionString);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(ConnectionString, prefs.getDatabaseUser(), prefs.getDatabasePassword());
            Log.i("Android", connection.toString());
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

//    public Connection ConnectToPICDB(Prefs prefs) {
//        {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            Connection connection = null;
//            try {
//                String ConnectionString = "jdbc:jtds:sqlserver://heartsun.redirectme.net:2022/"+"AGKPICDB7778"+";encrypt=fasle;user=rajesh;password=1234560";
//                Class.forName("net.sourceforge.jtds.jdbc.Driver");
//                connection = DriverManager.getConnection(ConnectionString, "rajesh", "1234560");
//            } catch (SQLException se) {
//                Log.e("SQL Error: ", se.getMessage());
//            } catch (ClassNotFoundException e) {
//                Log.e("Class Not Found : ", e.getMessage());
//            } catch (Exception e) {
//                Log.e("General Exception: ", e.getMessage());
//            }
//            return connection;
//        }
//    }

}
