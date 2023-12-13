package com.uob.testingout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    

    public static void main(String[] args) throws SQLException {
        Logger LOGGER = Logger.getLogger(LoginPage.class.getName());
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(
                    "/Users/bryanng77/Desktop/uob_capstone/uobbank/uobcapstone/src/main/java/com/uob/testingout/loggingFile.log",true); // specify file path for log file and set append = true (to prevent creation of multiple log files)
        } catch (Exception e) {
            System.out.println("error creating logger");
            return;
        }
        fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
        for (Handler h : LOGGER.getHandlers()) { // this portion of the code is used to prevent the message from printing in the Console
            LOGGER.removeHandler(h);
        }
        LogManager.getLogManager().reset(); // we have to use LogManager to reset the process so that the removeHandler() will be applied.
        LOGGER.addHandler(fileHandler);

   
        LoginPage.landingScreen();

    }
}
