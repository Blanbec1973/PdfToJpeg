
package model.steps;

import model.ProcessingContext;
import view.IUserInterface;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AskUserConfirmationStepTest {

    private Logger logger;
    private ProcessingContext context;
    private IUserInterface viewUI;
    private AskUserConfirmationStep step;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        viewUI = mock(IUserInterface.class);
        context = mock(ProcessingContext.class);
        step = spy(new AskUserConfirmationStep(logger));
    }

    @Test
    void shouldProceedWhenUserConfirms() {
        // Prépare le contexte avec un fichier
        File file = new File("test.pdf");
        when(context.getMostRecentFile()).thenReturn(file);
        when(context.getUI()).thenReturn(viewUI);
        when(viewUI.askUserForConfirmation(anyString())).thenReturn(true);
        doReturn(true).when(step).handleNext(context);

        boolean result = step.handle(context);

        assertTrue(result, "Le step doit retourner true si l'utilisateur confirme");
        verify(viewUI).askUserForConfirmation(contains("Proceed file"));
        verify(step).handleNext(context);
    }

    @Test
    void shouldReturnFalseWhenUserCancels() {
        File file = new File("test.pdf");
        when(context.getMostRecentFile()).thenReturn(file);
        when(context.getUI()).thenReturn(viewUI);
        when(viewUI.askUserForConfirmation(anyString())).thenReturn(false);

        boolean result = step.handle(context);

        assertFalse(result, "Le step doit retourner false si l'utilisateur refuse");
        verify(logger).info(contains("User cancel program."));
        verify(step, never()).handleNext(context);
    }
}