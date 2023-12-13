package com.uob.testingout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Teller {
    private String teller_username;
    private int teller_password;

    // Constructors
    public Teller(String teller_username, int teller_password) {
        this.teller_username = teller_username;
        this.teller_password = teller_password;
    }

    // Getters and Setters
    public String getTeller_username() {
        return teller_username;
    }

    public void setTeller_username(String teller_username) {
        this.teller_username = teller_username;
    }

    public int getTeller_password() {
        return teller_password;
    }

    public void setTeller_password(int teller_password) {
        this.teller_password = teller_password;
    }

    // toString()
    @Override
    public String toString() {
        return "tellerinfo [teller_username=" + teller_username + ", teller_password=" + teller_password + "]";
    }

    public void tellerInput() {
        int attempts = 0; // number of attempts upon start of program
        int maximum_attempts = 3; // maximum number of attempts

        int maxLengthOfUsername = 10;
        int maxLengthOfPassword = 10;

        // Declaring the scanner object
        Scanner scan = new Scanner(System.in);

        while (attempts < maximum_attempts) {
            System.out.print("Please enter employee/teller username: ");
            String tUsername = scan.nextLine();

            if (tUsername.length() > maxLengthOfUsername) {
                System.out.println("Please limit your Username to 10 characters.");
                continue;
            }

            System.out.print("Please enter employee/teller password: ");
            String tPassword = scan.nextLine();

            if (tPassword.length() > maxLengthOfPassword) {
                System.out.println("Please limit your Password to 10 characters.");
                continue;
            }

            // // Creating Object
            // tellerinfo info = new tellerinfo(tUsername, tPassword);

            // Testing MySQL database connectivity
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/uob_capstone", "root",
                        "bryanng77");

                String sqlStatement = ("SELECT * FROM teller WHERE username = ? AND password = ?; ");

                // The line PreparedStatement stmt = con.prepareStatement(sqlStatement); is
                // creating a PreparedStatement object in Java for executing a SQL query
                PreparedStatement stmt = con.prepareStatement(sqlStatement);

                stmt.setString(1, tUsername);
                stmt.setString(2, tPassword);

                // The line int results = stmt.executeUpdate(); is executing the SQL statement
                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    System.out.println("Login successful!"); // TO DO: SUCCESSFUL LOG INFO
                    break;

                    // ADD SHOW MENUUUU

                } else {
                    System.out.println("User does not exist or incorrect credentials. Please try again."); // TO DO: FAIL LOG ERROR
                    attempts++; // increasing the user attempt by 1
                    System.out.println("You have " + (maximum_attempts - attempts) + " remaining.");

                }
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

        // This runs when we're out of the while loop
        System.out.println("Maximum login attempts reached. Exiting...");

    }

    // Just trying to execute

}
