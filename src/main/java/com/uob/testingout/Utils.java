package com.uob.testingout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.sql.Timestamp;


public class Utils {
    public static int IDIncrementor(Connection con, String idColumnName, String tableName) throws SQLException {
        HashSet<Integer> maxID = new HashSet<Integer>();
        String sql = "SELECT MAX(" + idColumnName + ") FROM " + tableName + "";
        PreparedStatement getMaxID = con.prepareStatement(sql);
        ResultSet rsTwo = getMaxID.executeQuery();
        while (rsTwo.next()) {
            maxID.add(0);
            maxID.add(rsTwo.getInt(1));
        }
        int newID;

        if(Collections.max(maxID) == 0)
        {
            newID = 100;
        }

        else 
        {
            newID = Collections.max(maxID) + 1;
        }
        
        return newID;

    }

    public static HashSet<String> getColumnFromTable(Connection con, String columnName, String tableName) throws SQLException {
        HashSet<String> column = new HashSet<String>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT " + columnName + " FROM " + tableName + "");
        while (rs.next()) {
            column.add(rs.getString(1));
        }

        return column;
    }


    public static void createTransaction(Connection con, int transactionID, int accID, int empID,
            String transactionType, boolean transactionIsValid,
            Double transactionAmt, Timestamp date) throws SQLException {
        String sqlstatement = ("INSERT INTO Transactions VALUES(?, ?, ?, ?, ?, ?, ?);");
        PreparedStatement stmt = con.prepareStatement(sqlstatement);

        stmt.setInt(1, Utils.IDIncrementor(con, "transactionID", "Transactions"));
        stmt.setInt(2, accID);
        stmt.setInt(3, empID);
        stmt.setString(4, transactionType);
        stmt.setDouble(5, transactionAmt);
        stmt.setBoolean(6, transactionIsValid);
        stmt.setTimestamp(7, date);

        int result = stmt.executeUpdate();
        if (result == 0) {
            System.out.println("Transaction Failed");
        } else {
            System.out.println("Transaction Success");
        }

    }

}
