package control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArgsChecker {
    private static final Logger logger = LogManager.getLogger(ArgsChecker.class);
    private final String directory;

    public String getDirectory() {return directory;}

    public ArgsChecker(String[] args) {
        //First argument present :
        if (args.length == 0) {
            logger.error("No argument, end of program.");
            System.exit(-1);
        }
        directory = calculateDirectory(args[0]);
    }

    private String calculateDirectory(String arg) {
        if (arg.isEmpty()) {
            if (logger.isInfoEnabled())
                logger.info("First argument empty, replaced by : {}",System.getProperty("user.dir"));
            return System.getProperty("user.dir");
        }

        return arg;
    }
}
