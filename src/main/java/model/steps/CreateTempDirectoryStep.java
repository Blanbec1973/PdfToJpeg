package model.steps;

import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;
import java.io.File;

public class CreateTempDirectoryStep extends AbstractProcessingStep {
    public CreateTempDirectoryStep(Logger logger) {
        super(logger);
    }

    @Override
    public boolean handle(ProcessingContext context) {
        File tempDir = createTempDirectory("TEMP"+System.currentTimeMillis());
        context.setTempDir(tempDir);
        return handleNext(context);
    }

    public File createTempDirectory(String pathTemp) {
        File tempDir = new File(System.getProperty("java.io.tmpdir"),pathTemp);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        logger.info("Temp directory : {}.", tempDir.getAbsolutePath());
        return tempDir;
    }
}
