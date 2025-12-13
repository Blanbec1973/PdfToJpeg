package model.steps;


import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;

import java.io.File;

public class CalculateRootFileNameStep extends AbstractProcessingStep {
    public CalculateRootFileNameStep(Logger logger) {
        super(logger);
    }

    @Override
    public boolean handle(ProcessingContext context) {
        String rootFileName = calculateRootFileName(context.getMostRecentFile());
        if (rootFileName == null) {
            System.out.println("Nom racine non calculé.");
            return false;
        }
        context.setRootFileName(rootFileName);
        logger.info ("Target file name : {}",rootFileName);
        return handleNext(context);
    }

    private String calculateRootFileName(File mostRecentFile) {
        logger.info("Calculate target file name.");
        String tempName = mostRecentFile.getName();
        int first = tempName.indexOf("-")+1;
        int end = tempName.indexOf("-",first);
        String temp = tempName.substring(first, end);
        int fileNumber = Integer.parseInt(temp.trim());

        if (fileNumber == 12 || fileNumber == 22 || fileNumber == 32) {
            String tempDest1 = tempName.replace("- 12 -","- 13 -");
            String tempDest2 = tempDest1.replace("- 22 -", "- 23 -");
            String tempDest3 = tempDest2.replace("- 32 -","- 33 -");
            String tempDest4 = tempDest3.replace(".PDF", "");
            return tempDest4.replace(".pdf", "");
        }
        logger.info("Bad file number, expected 12 or 22, founded : {}", fileNumber);
        return null ;
    }
}
