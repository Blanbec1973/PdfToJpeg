package control;

import model.DocumentPDFv1;
import model.IDocumentPDF;
import model.MyFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.View;

import java.io.IOException;


public class Control {
    private static final Logger logger = LogManager.getLogger(Control.class);

    public static void main(String[] args) throws IOException {
        new Control();
    }

    Control() throws IOException {
        long start = System.currentTimeMillis();
        logger.info("Starting PdfToJpeg");
        Parameters parameters = new Parameters("config.properties");
        logger.info("PdfToJpeg version v{}",parameters.getVersion());
        MyFileUtils myFileUtils = new MyFileUtils();
        myFileUtils.setRootDirectory(parameters.getProperty("rootDirectory"));

        boolean continueProgram = myFileUtils.findFileToProcess();

        if (continueProgram) {
            View view = new View();
            continueProgram = view.askUserForConfirmation("Proceed file "+
                              myFileUtils.getMostRecentFile().getName() + " ?" );
        }

        if (continueProgram) {
            continueProgram = myFileUtils.calculateRootFileName(myFileUtils.getMostRecentFile());
        }

        if (continueProgram) {
            myFileUtils.createTempDirectory(parameters.getProperty("rootDirectory")+"TEMP"+System.currentTimeMillis());
        }

        if (continueProgram) {
            IDocumentPDF monDoc = new DocumentPDFv1(myFileUtils.getMostRecentFile());
            continueProgram = monDoc.convertPdfToJpeg(myFileUtils.getRootFileName(), myFileUtils.getTempDir());
        }

        if (continueProgram) {
            myFileUtils.copyDirectoryTemp();
        }

        if (myFileUtils.getTempDir() != null) {
            FileUtils.deleteDirectory(myFileUtils.getTempDir());
            logger.info("Deleting Temp directory {}.", myFileUtils.getTempDir());
        }
        logger.info("PDfToJpeg done in {} ms", System.currentTimeMillis() - start);
    }
}
