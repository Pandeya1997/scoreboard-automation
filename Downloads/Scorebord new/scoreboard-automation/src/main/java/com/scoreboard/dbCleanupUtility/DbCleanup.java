package com.scoreboard.dbCleanupUtility;

import com.scoreboard.constants.CleanupQueries;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.DBConnection;
import com.scoreboard.util.DBUtils;
import com.scoreboard.util.TestBase;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbCleanup {
    static Logger logger = Logger.getLogger(DbCleanup.class);
    static Connection connection = null;
    static DBConnection dbcon = new DBConnection();
    public static void main(String[] args) {
        executeCleanup();
    }

    public static void executeCleanup() {
        String DB_URL="jdbc:mysql://13.205.56.59:3306/scoreboard_uat";
        String DB_USERNAME="scoreboard-uat-usr";
        String DB_PASSWORD="jdbcdncjdncgbf";
        Statement statement = null;

        try {
            connection =
                    getConn(DB_URL,
                            DB_USERNAME,
                            DB_PASSWORD);
            connection.setAutoCommit(false);

            statement = connection.createStatement();

            logger.info("Cleanup started...");

            for (String query : CleanupQueries.DELETE_QUERIES) {
                logger.info("Executing: " + query);
                statement.executeUpdate(query);
            }

            connection.commit();
            logger.info("Cleanup successful. Transaction committed.");

        } catch (Exception e) {

            logger.info("Error occurred. Rolling back...");
            e.printStackTrace();

            if (connection != null) {
                try {
                    connection.rollback();
                    logger.info("Rollback successful.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } finally {

            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConn(String dbUrl, String userName, String password) throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = dbcon.getConnection(dbUrl, userName, password);
        }
        return connection;
    }
}
