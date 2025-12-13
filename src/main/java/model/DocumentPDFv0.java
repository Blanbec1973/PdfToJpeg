package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.imageio.metadata.IIOMetadata;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DocumentPDFv0 implements IDocumentPDF {
    private static final Logger logger = LogManager.getRootLogger();
    private final PDDocument pdfDocument;

    public DocumentPDFv0(File mostRecentFile) throws IOException {
        pdfDocument = Loader.loadPDF(mostRecentFile);
    }

    @Override
    public boolean convertPdfToJpeg(String rootFileName, File tempDir) throws IOException {

            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);

            for (int page = 0; page < pdfDocument.getNumberOfPages(); ++page) {
                BufferedImage image2 = pdfRenderer.renderImageWithDPI(page, 150, ImageType.RGB);
                String outputFilename = rootFileName + "-page" + (page + 1) + ".jpg";
                ImageIO.write(image2, "JPEG", new File(tempDir, outputFilename));
                logger.info("Creating file {}.", outputFilename);
            }
            return true;
    }

    @Override
    public void setDPI(IIOMetadata metadata) {
        throw new UnsupportedOperationException();
    }
}
