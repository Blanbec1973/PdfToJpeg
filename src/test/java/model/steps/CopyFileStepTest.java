
package model.steps;

import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CopyFileStepTest {

    private Logger logger;
    private ProcessingContext context;
    private CopyFileStep step;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        context = mock(ProcessingContext.class);
        step = spy(new CopyFileStep(logger));
    }

    @Test
    void shouldCopyFilesSuccessfully() throws IOException {
        // Prépare deux dossiers temporaires
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "tempSource");
        File rootDir = new File(System.getProperty("java.io.tmpdir"), "tempDest");
        tempDir.mkdir();
        rootDir.mkdir();

        // Ajoute un fichier dans le dossier source
        File file = new File(tempDir, "test.jpg");
        file.createNewFile();

        when(context.getTempDir()).thenReturn(tempDir);
        when(context.getRootDirectory()).thenReturn(rootDir);
        doReturn(true).when(step).handleNext(context);

        boolean result = step.handle(context);

        assertTrue(result, "Le step doit retourner true si la copie est réussie");
        verify(step).handleNext(context);
        assertTrue(new File(rootDir, "test.jpg").exists(), "Le fichier doit être copié dans le dossier destination");

        // Nettoyage
        file.delete();
        new File(rootDir, "test.jpg").delete();
        tempDir.delete();
        rootDir.delete();
    }

    @Test
    void shouldReturnFalseIfIOExceptionOccurs() {
        File fakeSource = new File("/root/forbidden"); // chemin inaccessible
        File fakeDest = new File(System.getProperty("java.io.tmpdir"), "dest");

        when(context.getTempDir()).thenReturn(fakeSource);
        when(context.getRootDirectory()).thenReturn(fakeDest);

        boolean result = step.handle(context);

        assertFalse(result, "Le step doit retourner false si une erreur survient");
        verify(logger).error(contains("Error while copying files"), anyString());
        verify(step, never()).handleNext(context);
     }
}