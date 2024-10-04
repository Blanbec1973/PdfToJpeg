package control;

import model.DocumentPDFv1;
import model.IDocumentPDF;
import model.MyFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.Parameter;
import view.View;

import java.io.IOException;



public class Control {
    private static final Logger logger = LogManager.getLogger(Control.class);

    public static void main(String[] args) throws IOException {
        new Control(args);
    }

    public Control(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        logger.info("Starting PdfToJpeg");
        Parameter parameters = new Parameter("config.properties");
        logger.info("PdfToJpeg version v{}",parameters.getVersion());
        ArgsChecker argsChecker = new ArgsChecker(args,parameters);
        MyFileUtils myFileUtils = new MyFileUtils();
        myFileUtils.setRootDirectory(argsChecker.getDirectory());

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
            myFileUtils.createTempDirectory("TEMP"+System.currentTimeMillis());
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
