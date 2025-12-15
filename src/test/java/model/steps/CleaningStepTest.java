
package model.steps;

import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CleaningStepTest {

    private Logger logger;
    private ProcessingContext context;
    private CleaningStep step;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        context = mock(ProcessingContext.class);
        step = spy(new CleaningStep(logger));
    }

    @Test
    void shouldDeleteTempDirectorySuccessfully() {
        // Crée un dossier temporaire
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "tempCleaning");
        tempDir.mkdir();

        when(context.getTempDir()).thenReturn(tempDir);
        doReturn(true).when(step).handleNext(context);

        boolean result = step.handle(context);

        assertTrue(result, "Le step doit retourner true si la suppression est réussie");
        assertFalse(tempDir.exists(), "Le dossier doit être supprimé");
        verify(step).handleNext(context);
        verify(logger).info(contains("Deleting Temp directory"), any(Object.class));
    }

    @Test
    void shouldReturnFalseIfTempDirIsNull() {
        when(context.getTempDir()).thenReturn(null);

        boolean result = step.handle(context);

        assertFalse(result, "Le step doit retourner false si tempDir est null");
        verify(step, never()).handleNext(context);
    }

    @Test
    void shouldReturnFalseIfIOExceptionOccurs() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "tempCleaningError");
        tempDir.mkdir();

        when(context.getTempDir()).thenReturn(tempDir);

        // Mock statique pour simuler IOException sur FileUtils.deleteDirectory
        try (var mockedFileUtils = Mockito.mockStatic(org.apache.commons.io.FileUtils.class)) {
            mockedFileUtils.when(() -> org.apache.commons.io.FileUtils.deleteDirectory(tempDir))
                    .thenThrow(new IOException("Erreur simulée"));

            boolean result = step.handle(context);

            assertFalse(result, "Le step doit retourner false si une erreur survient");
            verify(logger).error(contains("Error during deleting temp directory"), anyString());
            verify(step, never()).handleNext(context);
        }

        // Nettoyage manuel si nécessaire
        tempDir.delete();
    }
}