
package model.steps;

import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculateRootFileNameStepTest {

    private ProcessingContext context;
    private CalculateRootFileNameStep step;

    @BeforeEach
    void setUp() {
        Logger logger = mock(Logger.class);
        context = mock(ProcessingContext.class);
        step = spy(new CalculateRootFileNameStep(logger));
    }

    @Test
    void shouldCalculateRootFileNameSuccessfully() {
        // Fichier avec format attendu : "prefix- 12 -suffix.pdf"
        File file = new File("prefix- 12 -suffix.pdf");
        when(context.getMostRecentFile()).thenReturn(file);
        doReturn(true).when(step).handleNext(context);

        boolean result = step.handle(context);

        assertTrue(result, "Le step doit retourner true si le nom racine est calculé");
        verify(context).setRootFileName("prefix- 13 -suffix"); // Vérifie la transformation
        verify(step).handleNext(context);
    }

    @Test
    void shouldReturnFalseIfFileNameFormatIsInvalid() {
        // Fichier avec format incorrect
        File file = new File("invalidFileName.pdf");
        when(context.getMostRecentFile()).thenReturn(file);

        boolean result = step.handle(context);

        assertFalse(result, "Le step doit retourner false si le format est invalide");
        verify(context, never()).setRootFileName(any());
        verify(step, never()).handleNext(context);
    }
}
