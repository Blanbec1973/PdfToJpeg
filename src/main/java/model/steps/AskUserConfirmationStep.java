package model.steps;


import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;

public class AskUserConfirmationStep extends AbstractProcessingStep {
    public AskUserConfirmationStep(Logger logger) {
        super(logger);
    }

    @Override
    public boolean handle(ProcessingContext context) {
        String msg = "Proceed file "+ context.getMostRecentFile().getName() + " ?";
        boolean confirmed = context.getUI().askUserForConfirmation(msg);
        if (!confirmed) {
            logger.info("User cancel program.");
            return false;
        }
        return handleNext(context);
    }
}
