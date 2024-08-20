package view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class View {
    private static final Logger logger = LogManager.getLogger(View.class);

    public boolean askUserForConfirmation(String msg) {
        Scanner scanner = new Scanner(System.in);

        logger.info(msg);
        String userAnswer = scanner.next();

        scanner.close();

        if (userAnswer.equals("y")) {
            return true;
        }
        logger.info("User cancel program.");
        return false;

    }


}
