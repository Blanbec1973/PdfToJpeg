package model.steps;


import model.ProcessingContext;

public interface ProcessingStep {
    boolean handle(ProcessingContext context);
    void setNext(ProcessingStep next);
}
