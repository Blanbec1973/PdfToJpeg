package model.steps;

import model.ProcessingContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.Logger;

import java.io.IOException;

public class CleaningStep extends AbstractProcessingStep {
    public CleaningStep(Logger logger) {
        super(logger);
    }

    @Override
    public boolean handle(ProcessingContext context) {
        if (context.getTempDir() == null)
            return false;

        try {
            FileUtils.deleteDirectory(context.getTempDir());
        } catch (IOException e) {
            logger.error("Error during deleting temp directory : {}", e.getMessage());
            return false;
        }
        logger.info("Deleting Temp directory {}.", context.getTempDir());
        return handleNext(context);
    }
}
