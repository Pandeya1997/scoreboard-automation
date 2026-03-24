package com.scoreboard.util;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DBUtils {

    private Statement stmt;
    private ResultSet rs;
    public Logger logger = Logger.getLogger(TestBase.class);
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(
//                getInstance().getReadDatabaseUrls().get(TestBase.getEnv() + "DBUrl"),
//                getInstance().getReadDataBaseCredentials().get(TestBase.getEnv() + "DBUsername"),
//                getInstance().getReadDataBaseCredentials().get(TestBase.getEnv() + "DBPassword")
//        );
//    }


    public List<String> getValueFromDB(String query, Connection conn) {
        List<String> valueList = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                while (rs.next()) {
                    valueList.add(rs.getString(1));
                }
            }
        } catch (Exception e) {
            logger.info("Query didn't return any value" + query);
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                rs.close();
            } catch (SQLException sqe) {
                logger.debug("Statement didn't close properly..." + sqe.getMessage());
            }
            stmt = null;
            rs = null;
        }
        return valueList;
    }

    public List<String> getValuesFromDatabase(Connection connection, String query) {

        List<String> values = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                values.add(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    public void execute(String query, Connection conn) throws SQLException {
        try {
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(query);
            if (i > 0) {
                logger.info(i + " rows effected for query : " + query);
            } else {
                logger.info("Zero rows effected for query : " + query);
            }
        } catch (SQLException e) {
            logger.info("Error occured while executing Query : " + query);
            logger.error(e.getMessage());
            throw new SQLException(e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException sqe) {
                logger.debug("Statement didn't close properly..." + sqe.getMessage());
            }
            stmt = null;
        }
    }

    public String getStringColumnValueFromDB(String query, Connection conn, String columnLabel) {
        String val = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                val = rs.getString(columnLabel);
            }
        } catch (Exception e) {
            logger.info("Query didn't return any value: " + query);
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null && !stmt.isClosed())
                    stmt.close();
                if (rs != null && !rs.isClosed())
                    rs.close();
            } catch (SQLException sqe) {
                logger.debug("Statement didn't close properly..." + sqe.getMessage());
            }
        }
        return val;
    }

    public HashMap<String, List<String>> getValuesAsMap(String query, Connection conn) {
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultMetaData = null;
        LinkedHashMap<String, List<String>> result = new LinkedHashMap<>();
        try {
            statement = conn.createStatement();
            logger.debug("Executing Query : " + query);
            resultSet = statement.executeQuery(query);
            resultMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                for (int i = 1; i <= resultMetaData.getColumnCount(); i++) {
                    String strColumnName = resultMetaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    if (result.containsKey(strColumnName)) {
                        result.get(strColumnName).add(columnValue);
                    } else {
                        List<String> resultList = new ArrayList<>();
                        resultList.add(columnValue);
                        result.put(strColumnName, resultList);
                    }
                }
            }
        } catch (SQLException sqe) {
            logger.debug("Statement didn't close properly..." + sqe.getMessage());
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != statement) {
                    statement.close();
                }
                if (null != resultSet) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error("Error occured while closing the connection");
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }


}