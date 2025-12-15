package model.steps;


import model.IDocumentPDF;
import model.ProcessingContext;
import org.apache.logging.log4j.core.Logger;

import java.io.File;
import java.io.IOException;

public class ConvertPdfToJpegStep extends AbstractProcessingStep {
    public ConvertPdfToJpegStep(Logger logger) {
        super(logger);
    }

    @Override
    public boolean handle(ProcessingContext context) {
        boolean success;
        // Utilisation de DocumentPDFv1 ou DocumentPDFv0
        try {
            IDocumentPDF monDoc = createDocument(context.getMostRecentFile());
            success = monDoc.convertPdfToJpeg(context.getRootFileName(), context.getTempDir());
        } catch (IOException e) {
            logger.error("Error while converting PDF to JPEG : {}", e.getMessage());
            return false;
        }
        if (!success) {
            logger.error("Error while converting PDF to JPEG");
            return false;
        }
        return handleNext(context);
    }

    protected IDocumentPDF createDocument(File file) throws IOException {
        return new model.DocumentPDFv1(file);
    }
}
