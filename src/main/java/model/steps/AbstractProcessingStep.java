package model.steps;


import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;

public abstract class AbstractProcessingStep implements ProcessingStep {
    protected ProcessingStep next;
    protected Logger logger;

    public AbstractProcessingStep(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void setNext(ProcessingStep next) {
        this.next = next;
    }

    protected boolean handleNext(ProcessingContext context) {
        if (next != null) {
            return next.handle(context);
        }
        return true;
    }
}
