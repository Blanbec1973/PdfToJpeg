
package model.steps;

import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindFileStepTest {

    private ProcessingContext context;
    private FindFileStep step;

    @BeforeEach
    void setUp() {
        Logger logger = mock(Logger.class);
        context = mock(ProcessingContext.class);
        step = spy(new FindFileStep(logger));
    }

    @Test
    void shouldFindPdfFileAndCallNext() {
        // Préparation d’un dossier temporaire avec un fichier PDF
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "testFindPdf");
        tempDir.mkdir();
        File pdfFile = new File(tempDir, "test.pdf");
        try {
            pdfFile.createNewFile();

            when(context.getRootDirectory()).thenReturn(tempDir);
            // On simule le chainage des steps
            doReturn(true).when(step).handleNext(context);

            boolean result = step.handle(context);

            assertTrue(result, "Le step doit retourner true si un fichier est trouvé");
            verify(context).setMostRecentFile(pdfFile);
            verify(step).handleNext(context);
        } catch (Exception e) {
            fail("Erreur lors de la préparation du test : " + e.getMessage());
        } finally {
            pdfFile.delete();
            tempDir.delete();
        }
    }

    @Test
    void shouldReturnFalseIfNoPdfFound() {
        // Préparation d’un dossier temporaire vide
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "testNoPdf");
        tempDir.mkdir();

        when(context.getRootDirectory()).thenReturn(tempDir);

        boolean result = step.handle(context);

        assertFalse(result, "Le step doit retourner false si aucun fichier n'est trouvé");
        verify(context, never()).setMostRecentFile(any());
        verify(step, never()).handleNext(context);

        tempDir.delete();
    }
}
