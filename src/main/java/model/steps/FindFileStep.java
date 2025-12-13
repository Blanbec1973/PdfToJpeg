package model.steps;


import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;

import java.io.File;

public class FindFileStep extends AbstractProcessingStep {
    public FindFileStep(Logger logger) {
        super(logger);
    }

    @Override
    public boolean handle(ProcessingContext context) {
        // Logique pour trouver le fichier PDF le plus récent
        File file = findFileToProcess(context.getRootDirectory());
        if (file == null) {
            logger.info("No file founded.");
            return false;
        }
        context.setMostRecentFile(file);
        return handleNext(context);
    }

    private File findFileToProcess(File rootDirectory) {
        logger.info("Try to find file in {}.", rootDirectory);

        File[] files = rootDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        File mostRecentFile = null;
        if (files == null || files.length == 0) {
            logger.info("No pdf files in directory {}", rootDirectory);
            return null;
        }


        long mostRecentTimestamp = Long.MIN_VALUE;

        for (File file : files) {
            long lastModifiedTimestamp = file.lastModified();
            if (lastModifiedTimestamp > mostRecentTimestamp) {
                mostRecentFile = file;
                mostRecentTimestamp = lastModifiedTimestamp;
            }
        }

        if (mostRecentFile == null) {
            return null;
        }

        logger.info("File found : {}", mostRecentFile.getAbsolutePath());
        return mostRecentFile;
    }

}
