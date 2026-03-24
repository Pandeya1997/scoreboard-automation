package com.scoreboard.util;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Logger logger = Logger.getLogger(DBConnection.class);
    private Connection conn;


    public Connection getConnection(String hostName, String userName,
                                    String password) throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.debug("Connecting to" + hostName);
            System.out.println("Connecting to" + hostName);
            conn = DriverManager.getConnection(hostName, userName, password);

        } catch (ClassNotFoundException e) {
            logger.info("Connection object got created" + conn);
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                con = null;
                logger.info("Connection object got closed properly" + conn);
                e.printStackTrace();
            }
        }
    }
}
