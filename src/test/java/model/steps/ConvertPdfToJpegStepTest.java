
package model.steps;

import model.ProcessingContext;
import model.IDocumentPDF;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConvertPdfToJpegStepTest {

    private Logger logger;
    private ProcessingContext context;
    private ConvertPdfToJpegStep step;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        context = mock(ProcessingContext.class);
        step = spy(new ConvertPdfToJpegStep(logger));
    }

    @Test
    void shouldConvertPdfSuccessfully() throws IOException {
        // Prépare le contexte
        File pdfFile = new File("test.pdf");
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "tempConvert");
        tempDir.mkdir();

        when(context.getMostRecentFile()).thenReturn(pdfFile);
        when(context.getTempDir()).thenReturn(tempDir);
        when(context.getRootFileName()).thenReturn("output");

        // Mock du DocumentPDF
        IDocumentPDF mockDoc = mock(IDocumentPDF.class);
        when(mockDoc.convertPdfToJpeg(anyString(), any(File.class))).thenReturn(true);

        // Remplace la création réelle par notre mock
        doReturn(mockDoc).when(step).createDocument(any(File.class));
        doReturn(true).when(step).handleNext(context);

        boolean result = step.handle(context);

        assertTrue(result, "Le step doit retourner true si la conversion réussit");
        verify(mockDoc).convertPdfToJpeg(eq("output"), eq(tempDir));
        verify(step).handleNext(context);

        // Nettoyage
        tempDir.delete();
    }

    @Test
    void shouldReturnFalseIfIOExceptionOccurs() throws IOException {
        File pdfFile = new File("test.pdf");
        when(context.getMostRecentFile()).thenReturn(pdfFile);

        // Simule une IOException lors de la création du document
        doThrow(new IOException("Erreur simulée")).when(step).createDocument(any(File.class));

        boolean result = step.handle(context);

        assertFalse(result, "Le step doit retourner false si une IOException survient");
        verify(logger).error(contains("Error while converting PDF to JPEG"), anyString());
        verify(step, never()).handleNext(context);
    }
}