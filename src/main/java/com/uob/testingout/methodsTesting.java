package com.uob.testingout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class methodsTesting {

    public static void tellerinput() {
        // Declaring the scanner object

        Scanner scan = new Scanner(System.in);

        System.out.print("Please enter employee/teller username: ");
        String tUsername = scan.nextLine();

        System.out.print("Please enter employee/teller password: ");
        String tPassword = scan.nextLine();

        // // Creating Object
        // tellerinfo info = new tellerinfo(tUsername, tPassword);

        // Testing MySQL database connectivity
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/uob_capstone", "root",
                    "bryanng77");

            String sqlStatement = ("SELECT * FROM Teller WHERE username = ? AND password = ?; ");

            // The line PreparedStatement stmt = con.prepareStatement(sqlStatement); is
            // creating a PreparedStatement object in Java for executing a SQL query
            PreparedStatement stmt = con.prepareStatement(sqlStatement);

            stmt.setString(1, tUsername);
            stmt.setString(2, tPassword);

            // The line int results = stmt.executeUpdate(); is executing the SQL statement 
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed. User does not exist or incorrect credentials. Please try again.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        

    }

    public static void main(String[] args) {
        tellerinput();
    }
}
