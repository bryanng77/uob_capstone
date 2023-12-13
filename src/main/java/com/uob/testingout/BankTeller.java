package com.uob.testingout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankTeller {

    private int empID;
    private String fname;
    private String username;
    private String password;
    private static final Logger LOGGER = Logger.getLogger(LoginPage.class.getName()); // for logging purposes

    // Constructors
    public BankTeller(int empID, String fname, String username, String password) {
        this.empID = empID;
        this.fname = fname;
        this.username = username;
        this.password = password;
    }

    // Getter and Setters
    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Scanner getScan() {
        return scan;
    }

    public static void setScan(Scanner scan) {
        BankTeller.scan = scan;
    }

    @Override
    public String toString() {
        return "BankTeller [empID=" + empID + ", fname=" + fname + ", username=" + username + ", password=" + password
                + "]";
    }

    public boolean containsDigits(String name) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;

    }

    // SCANNER
    static Scanner scan = new Scanner(System.in);

    // VIEW ALL ACCOUNTS METHOD
    public void viewAllAccounts(Connection con) throws SQLException {
        LOGGER.log(Level.INFO, "Entering View All Accounts Selection.");
        // not using PreparedStatement as we don't have any conditions to filter
        String sqlstatement = ("SELECT * FROM BankAccount;");
        Statement stmt = con.createStatement();
        ResultSet result = stmt.executeQuery(sqlstatement);

        System.out.printf("%-11s | %-15s | %-15s | %-10s | %-10s  %n", "AccID", "First Name", "Last Name",
                "Account Type", "Account Balance");
        System.out.println("================================================================================");

        // Need to Loop Through to get each iteration result
        while (result.next()) {
            System.out.printf("%-11d | %-15s | %-15s | %-12s | %-10.2f  %n", result.getInt(1), result.getString(2),
                    result.getString(3), result.getString(4), result.getDouble(5));
        }

        LOGGER.log(Level.INFO, "View All Accounts Successful.");

        System.out.println("Press ENTER/RETURN to return to main menu");
        scan.nextLine();

        LOGGER.log(Level.INFO, "Returning back to Bank Teller Menu Page.");

        runBankTeller(con);
    }

    // CREATE ACCOUNT METHOD
    public void createAccount(Connection con) throws SQLException {
        LOGGER.log(Level.INFO, "Entering Create Accounts Selection.");
        System.out.println("===========================");
        System.out.println("- Create New User Account -");
        System.out.println("===========================");

        String fName = null;
        String lName = null;
        String accType = null;
        Double acctBal = null;

        try {

            // AccID will be instantiated and created through the Utils class file that we
            // have

            // First Name
            boolean fNameEmpty = false;

            while (!fNameEmpty) {
                try {
                    LOGGER.log(Level.INFO, "Entering Account Holder's First Name Information into the page");
                    System.out.print("Please Enter Account Holder's First Name: ");
                    fName = scan.nextLine();
                    if (fName.isEmpty()) {
                        System.out.println("Error: Empty Field!!");
                        LOGGER.log(Level.WARNING, "The First Name input was empty. User to try again.");
                    } else if (containsDigits(fName)) {
                        System.out.println("Please enter alphabetical letters only");
                        LOGGER.log(Level.WARNING, "The First Name input contained Numerical data, when it could ONLY have alphabetical data. User to try again.");

                    } else {
                        fNameEmpty = true;
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "An unexpected error occured: " + e.getMessage());
                    System.out.println("An unexpected error occurred: " + e.getMessage());

                }
            }

            boolean lNameEmpty = false;

            // Last Name
            while (!lNameEmpty) {
                try {
                    LOGGER.log(Level.INFO, "Entering Account Holder's Last Name Information into the page");
                    System.out.print("Please Enter Account Holder's Last Name: ");
                    lName = scan.nextLine();
                    if (lName.isEmpty()) {
                        System.out.println("Error: Empty Field!!");
                        LOGGER.log(Level.WARNING, "The Last Name input was empty. User to try again.");
                    } else if (containsDigits(lName)) {
                        System.out.println("Please enter alphabetical letters only");
                        LOGGER.log(Level.WARNING, "The Last Name input contained Numerical data, when it could ONLY have alphabetical data. User to try again.");

                    } else {
                        lNameEmpty = true;
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "An unexpected error occured: " + e.getMessage());
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            }

            // Account Type
            boolean validAccType = false;
            boolean emptyAccType = false;

            try {
                while (!emptyAccType) {
                    LOGGER.log(Level.INFO, "Entering Customers Account Type Information into the page");
                    System.out.print("Please Enter the Account Type (Savings/Current/Investment): ");
                    accType = scan.nextLine().trim();
            
                    if (accType.isEmpty()) {
                        System.out.println("Error: Account Type cannot be empty! Please try again.");
                        LOGGER.log(Level.WARNING, "The Account Type input was empty. User to try again.");
                    } else {
                        emptyAccType = true;
                    }
                }
            
                accType = accType.substring(0, 1).toUpperCase() + accType.substring(1).toLowerCase();
            
                while (!validAccType) {
                    LOGGER.log(Level.INFO, "Checking if the Account Type is valid");
                    if (accType.equals("Savings") || accType.equals("Current") || accType.equals("Investment")) {
                        validAccType = true;
                    } else {
                        System.out.println("Invalid Account Type! Please try again.");
                        LOGGER.log(Level.WARNING, "The Account Type input was invalid. User to try again.");
            
                        // Reset accType for re-entry
                        System.out.print("Please Enter the Account Type (Savings/Current/Investment): ");
                        accType = scan.nextLine().trim();
                        accType = accType.substring(0, 1).toUpperCase() + accType.substring(1).toLowerCase();
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "An unexpected error occurred: " + e.getMessage());
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
            
            // First Deposit Amount
            // has to be more than $1000, will have error if amount < 1000

            boolean validDeposit = false;

            try {
                while (!validDeposit) {
                    LOGGER.log(Level.INFO, "Entering Valid Deposits Infomation into the page");
                    System.out.print("Please Enter Mandatory First Deposit Amount: ");

                    // Check if the input is a valid double
                    if (scan.hasNextLine()) {
                        String input = scan.nextLine().trim();

                        if (input.isEmpty()) {
                            System.out.println("Error: First Deposit Amount cannot be empty! Please try again.");
                            LOGGER.log(Level.WARNING, "The Valid Deposit input was empty. User to try again.");
                            continue; // This process wull skip the rest of the loop and start over!!!
                        }

                        try {
                            acctBal = Double.parseDouble(input);

                            if (acctBal < 0) {
                                System.out.println("Error: First Deposit Amount cannot be negative! Please try again.");
                                LOGGER.log(Level.WARNING, "The Valid Deposit input was a negative amount. User to try again.");
                            } else if (acctBal <= 1000) {
                                System.out.println(
                                        "Error: The Deposit amount must be more than $1000. Please try again.");
                                LOGGER.log(Level.WARNING, "The Valid Deposit input was less than $1000. Deposit amount must be more than $1000. User to try again.");
                            } else {
                                validDeposit = true;
                            }
                        } catch (Exception e) {
                            System.out.println("Error: Invalid input. Please enter a valid number.");
                            LOGGER.log(Level.WARNING, "Invalid Input. User to try again.");
                        }
                    } else {
                        System.out.println("Error: Input is required. Please try again.");
                        scan.nextLine(); // Consume the invalid input to avoid an infinite loop
                    }
                }

                System.out.println("Entered deposit amount: $" + acctBal);
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            // Printing out all the details for double confirmation
            System.out.println("Please check if the details below are correct:");
            System.out.println("1. AccID: " + Utils.IDIncrementor(con, "accID", "BankAccount"));
            System.out.println("2. First Name: " + fName);
            System.out.println("3. Last Name: " + lName);
            System.out.println("4. Acounnt Type: " + accType);
            System.out.println("5. First Deposit Amount: " + acctBal);
            System.out.println("Are you sure you want to create this account? (y/n)");
            String confirmation = scan.nextLine();
            LOGGER.log(Level.INFO, "Double Confirmation of bank teller that the given information is correct.");

            if ("y".equalsIgnoreCase(confirmation)) {
                String sqlstatement = ("INSERT INTO BankAccount VALUES (?, ?, ?, ?, ?);");
                PreparedStatement stmt = con.prepareStatement(sqlstatement);
                stmt.setInt(1, Utils.IDIncrementor(con, "accID", "BankAccount"));
                stmt.setString(2, fName);
                stmt.setString(3, lName);
                stmt.setString(4, accType);
                stmt.setDouble(5, acctBal);

                int results = stmt.executeUpdate();
                System.out.println(results + " row has been successfully created in BankAccount.");
                LOGGER.log(Level.INFO, results + " row has been successfully created in BankAccount.");
            } else {
                System.out.println("An error has occured. Please try again.");
                LOGGER.log(Level.WARNING, "An error has occurred. User to try again.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            //
        }

        System.out.println("Press ENTER/RETURN to return to main menu");
        scan.nextLine();

        
        LOGGER.log(Level.INFO, "Returning back to Bank Teller Menu Page.");
        runBankTeller(con);

    }

    // DELETE ACCOUNTS METHOD
    public void deleteAccount(Connection con) throws SQLException {
        LOGGER.log(Level.INFO, "Entering Delete Accounts Selection.");

        System.out.println("===========================");
        System.out.println("- DELETE User Account -");
        System.out.println("===========================");

        boolean successfulDeletion = false;

        while (!successfulDeletion) {
            try {
                LOGGER.log(Level.INFO, "Entering ID of Customer to Delete Account Infomation");
                System.out.println("Please input ID of Customer you wish to DELETE. This process is irreversible.: ");
                String accIDStr = scan.nextLine().trim(); // read the input as a string
                if (accIDStr.isEmpty()) {
                    System.out.println("Error: Account ID cannot be empty. Please enter a valid Account ID.");
                    LOGGER.log(Level.WARNING, "The Valid Deposit input was empty. User to try again.");
                    continue; // restart the loop
                }

                int accID = Integer.parseInt(accIDStr); // parse the string as an integer
                System.out.println("Are you sure you wish to delete Account ID: " + accID + "? (y/n)");
                LOGGER.log(Level.INFO, "Double Confirmation to delete the account.");
                String confirmation = scan.nextLine();

                if ("y".equalsIgnoreCase(confirmation)) {
                    String sqlstatement = ("DELETE FROM BankAccount WHERE accID = ?;");
                    PreparedStatement stmt = con.prepareStatement(sqlstatement);
                    stmt.setInt(1, accID);
                    int deletedRows = stmt.executeUpdate();

                    if (deletedRows > 0) {
                        System.out.println("Success! Customer " + accID + " has been deleted from the database.");
                        LOGGER.log(Level.INFO, "Success! Customer " + accID + " has been deleted from the database.");
                        successfulDeletion = true;
                    } else {
                        System.out.println("Customer ID " + accID + " does not exist. Please enter a valid Customer ID.");
                        LOGGER.log(Level.WARNING, "Customer ID " + accID + " does not exist. Please enter a valid Customer ID.");
                    }
                }

            } catch (Exception e) {
                System.out.println("Account ID does not exist. Please enter a valid Account ID.");
                LOGGER.log(Level.WARNING, "Account ID does not exist. Please enter a valid Account ID.");
            } finally {

            }
            System.out.println("Press ENTER/RETURN to return to main menu");
            scan.nextLine();

            LOGGER.log(Level.INFO, "Returning back to the Bank Teller Menu Page.");
            runBankTeller(con);
        }

    }

    // DEPOSIT MONEY METHOD
    public void depositMoney(Connection con) throws SQLException {
        LOGGER.log(Level.INFO, "Entering Deposit Money Selection Page");
        System.out.println("===========================");
        System.out.println("- Deposit Money -");
        System.out.println("===========================");

        boolean successfulDeposit = false;

        while (!successfulDeposit) {
            try {
                LOGGER.log(Level.INFO, "Entering ID of Customer to Deposit Money into");
                System.out.println("Please input ID of Customer you wish to DEPOSIT money into: ");
                String accIdStr = scan.nextLine().trim();
                if (accIdStr.isEmpty()) {
                    System.out.println("Error: Account ID cannot be empty. Please enter a valid Account ID.");
                    LOGGER.log(Level.WARNING, "The Account ID input was empty. User to try again.");
                    continue;
                }

                int accID = Integer.parseInt(accIdStr);
                System.out.println("Are you sure you wish to DEPOSIT Money into Account ID: " + accID + "? (y/n)");
                LOGGER.log(Level.INFO, "Double Confirmation to deposit money into the account.");
                String confirmation = scan.nextLine();

                if ("y".equalsIgnoreCase(confirmation)) {
                    String sqlstatement = ("SELECT * FROM BankAccount WHERE accID = ?");
                    PreparedStatement stmt = con.prepareStatement(sqlstatement);
                    stmt.setDouble(1, accID);
                    ResultSet result = stmt.executeQuery();

                    if (result.next()) {
                        // This portion of the code is to check if there is a matching customer ID
                        LOGGER.log(Level.INFO, "Asking the customer how much he/she would like to deposit");
                        System.out.println("How much money would the customer like to DEPOSIT?: ");
                        double depositingAmount = scan.nextDouble();
                        scan.nextLine();

                        // Create Transaction
                        int newTrnID = Utils.IDIncrementor(con, "transactionID", "Transactions");
                        createTransaction(con, newTrnID, accID, empID, "Deposit", true, depositingAmount,
                                Timestamp.valueOf(LocalDateTime.now()));

                        // Updating Portion
                        String sqlUpdateDepositStatement = ("UPDATE BankAccount SET balance = balance + ? WHERE accID = ?;");
                        PreparedStatement stmt1 = con.prepareStatement(sqlUpdateDepositStatement);
                        stmt1.setDouble(1, depositingAmount);
                        stmt1.setInt(2, accID);
                        int updatedRow = stmt1.executeUpdate();

                        // has to be more than zero else it won't update
                        if (updatedRow > 0) {
                            System.out.println("The money has been SUCCESSFULLY DEPOSITED");
                            LOGGER.log(Level.INFO, depositingAmount + " has been successfully deposited into AccID " + accID);
                        } else {
                            System.out.println("Failure to update the balance. Please try again.");
                            LOGGER.log(Level.WARNING, "Failure to update the balance. User to try again.");
                        }

                    } else {
                        System.out.println("The Account ID " + accID + " does not exist. Please try again.");
                        LOGGER.log(Level.WARNING, "The Account ID " + accID + " does not exist. Please try again.");
                    }

                }

            } catch (Exception e) {
                // print error message
            }

            System.out.println("Press ENTER/RETURN to return to main menu");
            scan.nextLine();

            LOGGER.log(Level.INFO, "Returning back to the Bank Teller Menu Page.");
            runBankTeller(con);
        }
    }

    // WITHDRAW MONEY METHOD
    public void withdrawMoney(Connection con) throws SQLException {
        LOGGER.log(Level.INFO, "Entering Withdraw Money Selection Page");
        System.out.println("===========================");
        System.out.println("- Withdraw Money -");
        System.out.println("===========================");

        boolean successfulWithdraw = false;

        while (!successfulWithdraw) {
            try {
                LOGGER.log(Level.INFO, "Entering ID of Customer to Withdraw Money from");
                System.out.println("Please input ID of Customer you wish to WITHDRAW money from: ");
                String accIdStr = scan.nextLine().trim();
                if (accIdStr.isEmpty()) {
                    System.out.println("Error: Account ID cannot be empty. Please enter a valid Account ID.");
                    LOGGER.log(Level.WARNING, "The Account ID input was empty. User to try again.");
                    continue;
                }

                int accID = Integer.parseInt(accIdStr);
                System.out.println("Are you sure you wish to WITHDRAW Money from Account ID: " + accID + "? (y/n)");
                LOGGER.log(Level.INFO, "Double Confirmation to with moneyt from the account.");
                String confirmation = scan.nextLine();

                if ("y".equalsIgnoreCase(confirmation)) {
                    String sqlstatement = ("SELECT * FROM BankAccount WHERE accID = ?");
                    PreparedStatement stmt = con.prepareStatement(sqlstatement);
                    stmt.setDouble(1, accID);
                    ResultSet result = stmt.executeQuery();

                    if (result.next()) {
                        // This portion of the code is to check if there is a matching customer ID
                        LOGGER.log(Level.INFO, "Asking the customer how much he/she would like to withdraw");
                        System.out.println("How much money would the customer like to WITHDRAW?: ");
                        double withdrawingAmount = scan.nextDouble();
                        scan.nextLine();

                        // Create Transaction
                        int newTrnID = Utils.IDIncrementor(con, "transactionID", "Transactions");
                        createTransaction(con, newTrnID, accID, empID, "Withdraw", true, withdrawingAmount,
                                Timestamp.valueOf(LocalDateTime.now()));

                        // Updating Portion
                        String sqlUpdateWithdrawStatement = ("UPDATE BankAccount SET balance = balance - ? WHERE accID = ?;");
                        PreparedStatement stmt1 = con.prepareStatement(sqlUpdateWithdrawStatement);
                        stmt1.setDouble(1, withdrawingAmount);
                        stmt1.setInt(2, accID);
                        int updatedRow = stmt1.executeUpdate();

                        // has to be more than zero else it won't update
                        if (updatedRow > 0) {
                            System.out.println("The money has been SUCCESSFULLY WITHDRAWN");
                            LOGGER.log(Level.INFO, withdrawingAmount +" has been successfully withdrawn.");
                        } else {
                            System.out.println("Failure to update the balance. Please try again.");
                            LOGGER.log(Level.WARNING, "Failure to update the balance. User to try again.");
                        }

                    } else {
                        System.out.println("The Account ID " + accID + " does not exist. Please try again.");
                        LOGGER.log(Level.WARNING, "The Account ID " + accID + " does not exist. Please try again.");
                    }

                }

            } catch (Exception e) {
                // print error message
            }

            System.out.println("Press ENTER/RETURN to return to main menu");
            scan.nextLine();

            LOGGER.log(Level.INFO, "Returning back to the Bank Teller Menu Page.");
            runBankTeller(con);
        }
    }

    // EXIT APPLICATION METHOD
    public void exitApplication() {
        LOGGER.info("User Exited the System");
        System.out.println("Thank you and have a pleasant day ahead.");
        System.exit(0);

    }

    public void runBankTeller(Connection con) throws SQLException {

        System.out.println("Teller Function");
        System.out.println("===============================");
        System.out.println("1. View All Accounts");
        System.out.println("2. Create Account");
        System.out.println("3. Delete Account");
        System.out.println("4. Deposit Money");
        System.out.println("5. Withdraw Money");
        System.out.println("6. Back to Login");
        System.out.println("0. Exit Application");

        System.out.println("Please enter a input : ");
        // Scanner scan = new Scanner(System.in);
        int choice = 0;

        do {

            choice = Integer.parseInt(scan.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Selected 1. View All Accounts ");
                    System.out.println("==============================");
                    viewAllAccounts(con); // method from above
                    break;
                case 2:
                    System.out.println("Selected 2. Create Account ");
                    System.out.println("===========================");
                    createAccount(con); // method from above
                    break;
                case 3:
                    System.out.println("Selected 3. Delete Account ");
                    System.out.println("===========================");
                    deleteAccount(con); // method from above
                    break;
                case 4:
                    System.out.println("Selected 4. Deposit Money ");
                    System.out.println("==========================");
                    depositMoney(con); // method from above
                    break;
                case 5:
                    System.out.println("Selected 5. Withdraw Money");
                    System.out.println("==========================");
                    withdrawMoney(con); // method from above
                    break;
                case 6:
                    System.out.println("Selected 6. Back to Login Screen");
                    System.out.println("================================");
                    LOGGER.log(Level.INFO, "Returning back to Login Page");
                    LoginPage.landingScreen();
                    break;
                case 0:
                    System.out.println("Exit Application");
                    exitApplication(); // method from above
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 0 and 6.");
            }

        } while (choice < 0 || choice > 6);

    }

    public static void createTransaction(Connection con, int transactionID, int accID, int empID,
            String transactionType, boolean transactionIsValid,
            Double transactionAmt, Timestamp timestamp) throws SQLException {
        String sqlstatement = ("INSERT INTO Transactions VALUES(?, ?, ?, ?, ?, ?, ?);");
        PreparedStatement stmt = con.prepareStatement(sqlstatement);

        stmt.setInt(1, Utils.IDIncrementor(con, "transactionID", "Transactions"));
        stmt.setInt(2, accID);
        stmt.setInt(3, empID);
        stmt.setString(4, transactionType);
        stmt.setDouble(5, transactionAmt);
        stmt.setBoolean(6, transactionIsValid);
        stmt.setTimestamp(7, timestamp);

        int result = stmt.executeUpdate();
        if (result == 0) {
            System.out.println("Transaction Failed");
        } else {
            System.out.println("Transaction Success");
        }

    }

}
