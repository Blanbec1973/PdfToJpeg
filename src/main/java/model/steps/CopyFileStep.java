package model.steps;


import model.ProcessingContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.Logger;

import java.io.IOException;

public class CopyFileStep extends AbstractProcessingStep {
    public CopyFileStep(Logger logger) {
        super(logger);
    }

    @Override
    public boolean handle(ProcessingContext context) {
        try {
            FileUtils.copyDirectory(context.getTempDir(), context.getRootDirectory());
        } catch (IOException e) {
            logger.error("Error while copying files : {}", e.getMessage());
            return false;
        }
        logger.info("Copying files from {} to {}", context.getTempDir(), context.getRootDirectory());

        return handleNext(context);
    }
}
