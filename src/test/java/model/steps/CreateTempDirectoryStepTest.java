
package model.steps;

import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateTempDirectoryStepTest {

    private ProcessingContext context;
    private CreateTempDirectoryStep step;

    @BeforeEach
    void setUp() {
        Logger logger = mock(Logger.class);
        context = mock(ProcessingContext.class);
        step = spy(new CreateTempDirectoryStep(logger));
    }

    @Test
    void shouldCreateTempDirectoryAndCallNext() {
        // On simule la création d’un dossier temporaire
        doReturn(true).when(step).handleNext(context);

        boolean result = step.handle(context);

        assertTrue(result, "Le step doit retourner true si le dossier est créé et la suite appelée");
        // Vérifie que setTempDir a bien été appelé avec un dossier existant
        ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);
        verify(context).setTempDir(captor.capture());
        File tempDir = captor.getValue();
        assertNotNull(tempDir, "Le dossier temporaire ne doit pas être null");
        assertTrue(tempDir.exists(), "Le dossier temporaire doit exister sur le disque");
        verify(step).handleNext(context);

        // Nettoyage
        tempDir.delete();
    }

    @Test
    void shouldReturnExistingDirectoryIfAlreadyExists() {
        // On crée un dossier temporaire à l’avance
        String tempName = "TEMP" + System.currentTimeMillis();
        File tempDir = new File(System.getProperty("java.io.tmpdir"), tempName);
        tempDir.mkdir();

        // On appelle directement la méthode utilitaire
        File resultDir = step.createTempDirectory(tempName);

        assertEquals(tempDir.getAbsolutePath(), resultDir.getAbsolutePath(), "Le dossier existant doit être retourné");
        assertTrue(resultDir.exists(), "Le dossier doit exister");

        // Nettoyage
        tempDir.delete();
    }
}
