package com.uob.testingout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemAdmin {
    private int accID;
    private String name;
    private String username;
    private String password;
    private Scanner input;
    private static final Logger LOGGER = Logger.getLogger(LoginPage.class.getName()); // for logging purposes

    public SystemAdmin(int accID, String name, String username, String password) {
        this.accID = accID;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getAccID() {
        return accID;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean containsDigits(String fname) {
        for (int i = 0; i < fname.length(); i++) {
            char c = fname.charAt(i);
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;

    }

    public Scanner getScanner() {
        if (null == input)
            input = new Scanner(System.in);
        return input;
    }

    public String getString(String message, int maxLength, boolean canContainDigits, String fieldType) {
        input = getScanner();
        boolean validInput = false;
        String userInput = null;
        do {
            System.out.println(message);
            userInput = input.nextLine();
            if (!canContainDigits && containsDigits(userInput)) {
                System.out.println("Your name should not contain any digits. Please try again.");
                continue;
            }
            switch (fieldType) {
                case "name":
                case "username":
                    if (userInput.length() > maxLength) {
                        System.out.println("Your name is too long. Please try again.");
                        continue;
                    } else {
                        validInput = true;
                    }
                    break;
                case "password":
                    if (userInput.length() > maxLength) {
                        System.out.println("Your password is too long. Please try again.");
                        continue;
                    } else {
                        validInput = true;
                    }
                    break;
                default:
                    System.out.println("Unrecognized Field Input " + fieldType);
                    System.exit(1);
            }
        } while (!validInput);
        return userInput;
    }

    public void createTeller(Connection con) throws SQLException {
        LOGGER.log(Level.INFO, "Entering Teller Create Accounts Selection.");
        String fName = "";
        String username = "";
        String password = "";

        try {

            LOGGER.log(Level.INFO, "Entering Tellers Name Information into the page");
            fName = getString("Please input the Teller's name (less than 20 characters): ", 20, false, "name");
            do {
                LOGGER.log(Level.INFO, "Entering Tellers Username Information into the page");
                username = getString("Please input the Teller's username to log in (less than 20 characters): ", 20,
                        true, "username");
                if (Utils.getColumnFromTable(con, "username", "teller").contains(username)) {
                    System.out.println("This username already exists. Please try again.");
                    LOGGER.log(Level.WARNING, "Username already exists. User to try again.");
                }
            } while (Utils.getColumnFromTable(con, "username", "teller").contains(username));

            LOGGER.log(Level.INFO, "Entering Tellers Password Information into the page");
            password = getString("Please input the Teller's password to log in (less than 20 characters): ", 20, true,
                    "password");
            System.out.println("New Teller ID: " + Utils.IDIncrementor(con, "empID", "teller"));
            String sql = "INSERT INTO Teller VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, Utils.IDIncrementor(con, "empID", "teller"));
            stmt.setString(2, fName);
            stmt.setString(3, username);
            stmt.setString(4, password);
            int res = stmt.executeUpdate();
            System.out.println(res + " row successfully created in Teller.");
            LOGGER.log(Level.INFO, res + " row successfully created in Teller.");

        } catch (Exception e) {
            System.out.println("Exception occured: " + e);
            LOGGER.log(Level.WARNING, "An Error has ocurred. User to try again.");
        } finally {
            System.out.println("Press any key to return to main menu");
            input.nextLine();

            System.out.println("Press ENTER/RETURN to return to System Admin Menu Page");
            runSystemAdmin(con);
        }
    }

    public void viewTeller(Connection con) throws SQLException {
        LOGGER.log(Level.INFO, "Entering View All Tellers Selection.");
        String sql = "SELECT * FROM Teller";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        System.out.printf("%-11s%-21s%-21s%-21s%n", "EmpID", "Name", "Username", "Password");
        System.out.println("============================================================================");
        while (rs.next()) {
            System.out.printf("%-10d|%-20s|%-20s|%-20s%n",
                    rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4));
        }
        input = new Scanner(System.in);
        System.out.println("Press any key to return to main menu");
        input.nextLine();

        LOGGER.log(Level.INFO, "Returning back to System Admin Menu Page.");
        runSystemAdmin(con);
    }

    public void updateTeller(Connection con) throws SQLException {
            LOGGER.log(Level.INFO, "Entering Update Teller Selection.");
        try {
            String fName = "";
            String username = "";
            String password = "";
            HashSet<String> currUser = new HashSet<>();

            Statement selectAllUser = con.createStatement();
            ResultSet rs = selectAllUser.executeQuery("SELECT username FROM teller;");
            while (rs.next()) {
                currUser.add(rs.getString(1));
            }

            input = new Scanner(System.in);
            LOGGER.log(Level.INFO, "Entering ID of Teller to Update Account Infomation");
            System.out.println("Please input ID of Teller you wish to update: ");
            int empID = Integer.parseInt(input.nextLine());
            if (!Utils.getColumnFromTable(con, "empID", "teller").contains(String.valueOf(empID))) {
                throw new Exception("This ID does not exist.");
            }

            String sql = "UPDATE teller SET fName=?, username=?, password=? WHERE empID=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(4, empID);

            LOGGER.log(Level.INFO, "Entering the Teller Name Infomation - to update");
            fName = getString("Please input the Teller's name: ", 20, false, "name");
            do {
                LOGGER.log(Level.INFO, "Entering the Teller Username Infomatio - to update");
                username = getString("Please input the Teller's username: ", 20, true, "username");
                if (currUser.contains(username)) {
                    System.out.println("This username already exists. Please try again.");
                }
            } while (currUser.contains(username));

            LOGGER.log(Level.INFO, "Entering the Teller Password Infomation - to update");
            password = getString("Please input the Teller's password: ", 20, true, "password");
            stmt.setString(1, fName);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();
            System.out.println(empID + " has been updated successfully.");
            LOGGER.log(Level.INFO, "empID + \" has been updated successfully.\"");

        } catch (Exception e) {
            System.out.println("Exception ocurred: " + e);
        }

        finally {
            System.out.println("Press any key to return to main menu");
            input.nextLine();

            LOGGER.log(Level.INFO, "Returning back to System Admin Menu Page.");
            runSystemAdmin(con);
        }

    }

    public void deleteTeller(Connection con) throws SQLException {
        try {
            LOGGER.log(Level.INFO, "Entering Delete Teller Selection.");
            input = new Scanner(System.in);
            System.out.println("Please input ID of Teller you wish to delete (Please note deletes are permanent): ");
            int empID = Integer.parseInt(input.nextLine());
            if (!Utils.getColumnFromTable(con, "empID", "teller").contains(String.valueOf(empID))) {
                throw new Exception("This ID does not exist.");
            }
            System.out.println("Are you sure you wish to delete empID " + empID + "? (y/n)");
            LOGGER.log(Level.INFO, "Double Confirmation to delete the account.");

            String confirmation = input.nextLine();
            if (confirmation.trim().equalsIgnoreCase("y")) {
                String sql = "DELETE FROM teller WHERE empID=?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setInt(1, empID);
                stmt.executeUpdate();
                System.out.println("Success! Teller " + empID + " has been deleted from the database.");
                LOGGER.log(Level.INFO, "Success! Teller " + empID + " has been deleted from the database.");
                System.out.println("Press any key to return to main menu");
                input.nextLine();

                LOGGER.log(Level.INFO, "Returning back to System Admin Menu Page.");
                runSystemAdmin(con);
            } else {
                System.out.println("Cancelled delete action.");
                LOGGER.log(Level.INFO, "Cancel Delete Action");

                System.out.println("Press any key to return to main menu");
                input.nextLine();

                LOGGER.log(Level.INFO, "Returning back to System Admin Menu Page.");
                runSystemAdmin(con);
            }
        } catch (Exception e) {
            System.out.println("Exception occured: " + e);
            LOGGER.log(Level.WARNING, "An Error has ocurred. User to try again.");
        } finally {
            System.out.println("Press any key to return to main menu");
            input.nextLine();

            LOGGER.log(Level.INFO, "Returning back to System Admin Menu Page.");            
            runSystemAdmin(con);
        }
    }

    public void runSystemAdmin(Connection con) throws SQLException {
        System.out.println("System Admin Menu");
        System.out.println("======================");
        System.out.println("1. Create Teller");
        System.out.println("2. View Teller");
        System.out.println("3. Update Teller");
        System.out.println("4. Delete Teller");
        System.out.println("5. Back to Login Screen");       
        System.out.println("0. Log out");
        System.out.println("Please enter a input : ");
        Scanner scan = new Scanner(System.in);
        int choice = 0;

        do {

            choice = Integer.parseInt(scan.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Selected 1. Create Teller");
                    System.out.println("============================");
                    createTeller(con);
                    break;
                case 2:
                    System.out.println("Selected 2. View All Tellers");
                    System.out.println("============================");
                    viewTeller(con);
                    break;
                case 3:
                    System.out.println("Selected 3. Update Teller");
                    System.out.println("============================");
                    updateTeller(con);
                    break;
                case 4:
                    System.out.println("Selected 4. Delete Teller");
                    System.out.println("============================");
                    deleteTeller(con);
                    break;

                case 5:
                    System.out.println("Selected 5. Back to Login Screen");
                    System.out.println("================================");
                    LOGGER.log(Level.INFO, "Returning back to Login Page");
                    LoginPage.landingScreen();
                    break;

                case 0:
                    LOGGER.info("User Exited the System");
                    System.out.println("Thank you for using Bank Application. Goodbye.");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }

        } while (choice < 0 || choice > 4);
        scan.close();
    }

}
