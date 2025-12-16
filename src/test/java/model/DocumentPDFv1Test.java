
package model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentPDFv1Test {

    private File fakePdfFile;
    private PDDocument mockPdDocument;
    private PDFRenderer mockRenderer;
    private ImageWriter mockWriter;
    private ImageWriteParam mockWriteParam;
    private IIOMetadata mockMetadata;
    private ImageOutputStream mockStream;

    @BeforeEach
    void setUp() {
        fakePdfFile = new File("fake.pdf");
        mockPdDocument = mock(PDDocument.class);
        mockRenderer = mock(PDFRenderer.class);
        mockWriter = mock(ImageWriter.class);
        mockWriteParam = mock(ImageWriteParam.class);
        mockMetadata = mock(IIOMetadata.class);
        mockStream = mock(ImageOutputStream.class);
    }

    @Test
    void shouldConvertPdfToJpegSuccessfully() throws Exception {
        // Arrange
        when(mockPdDocument.getNumberOfPages()).thenReturn(1);
        BufferedImage fakeImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        when(mockRenderer.renderImageWithDPI(eq(0), anyFloat(), any())).thenReturn(fakeImage);
        when(mockWriter.getDefaultWriteParam()).thenReturn(mockWriteParam);
        when(mockWriter.getDefaultImageMetadata(any(), any())).thenReturn(mockMetadata);
        when(mockMetadata.isReadOnly()).thenReturn(false);
        when(mockMetadata.isStandardMetadataFormatSupported()).thenReturn(true);


        // Mock statiques
        try (MockedStatic<org.apache.pdfbox.Loader> loaderMock = mockStatic(org.apache.pdfbox.Loader.class);
             MockedStatic<ImageIO> imageIOMock = mockStatic(ImageIO.class)) {

            loaderMock.when(() -> org.apache.pdfbox.Loader.loadPDF(any(File.class)))
                    .thenReturn(mockPdDocument);

            imageIOMock.when(() -> ImageIO.getImageWritersByFormatName("jpeg"))
                    .thenReturn(Collections.singletonList(mockWriter).iterator());
            imageIOMock.when(() -> ImageIO.createImageOutputStream(any(File.class)))
                    .thenReturn(mockStream);

            // On évite l'appel réel à writer.write()
            doNothing().when(mockWriter).setOutput(any());
            doNothing().when(mockWriter).write(any(IIOMetadata.class), any(), any());

            // On évite l'appel réel à setDPI2 (optionnel)
            // doNothing().when(mockMetadata).mergeTree(anyString(), any());

            // Surcharge du renderer pour injecter le mock
            DocumentPDFv1 doc = new DocumentPDFv1(fakePdfFile) {
                @Override
                protected PDFRenderer createRenderer(PDDocument doc) {
                    return mockRenderer;
                }
            };

            boolean result = doc.convertPdfToJpeg("output", new File(System.getProperty("java.io.tmpdir")));

            assertTrue(result, "La conversion doit réussir");
            verify(mockRenderer).renderImageWithDPI(eq(0), eq(200f), any());
            verify(mockWriter).write(eq(mockMetadata), any(), eq(mockWriteParam));
        }
    }

    @Test
    void shouldThrowIOExceptionIfPdfInvalid() {
        try (MockedStatic<org.apache.pdfbox.Loader> loaderMock = mockStatic(org.apache.pdfbox.Loader.class)) {
            loaderMock.when(() -> org.apache.pdfbox.Loader.loadPDF(any(File.class)))
                    .thenThrow(new IOException("PDF invalide"));

            assertThrows(IOException.class, () -> new DocumentPDFv1(fakePdfFile));
        }
    }
}