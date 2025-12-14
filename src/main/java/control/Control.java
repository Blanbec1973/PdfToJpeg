package control;

import model.ProcessingContext;
import model.steps.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.heyner.common.Parameter;
import view.ViewUI;
import view.ViewUIAppender;


public class Control {

    public static void main(String[] args) {
        Logger logger = (Logger) LogManager.getRootLogger();
        try {
            run(args, logger);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
            logger.error("Initialization error : {}", e.getMessage());
        }
    }

    public static void run(String[] args, Logger logger) {
        long start = System.currentTimeMillis();
        ViewUI viewUI = new ViewUI();

        ViewUIAppender appender = ViewUIAppender.createAppender();
        appender.start();
        logger.addAppender(appender);
        logger.setAdditive(true); // Keep other appends (console, file)

        logger.info("Starting PdfToJpeg");

        Parameter parameters = new Parameter("config.properties");
        logger.info("PdfToJpeg version v{}",parameters.getVersion());

        ArgsChecker argsChecker = new ArgsChecker(args);
        ProcessingContext context = new ProcessingContext(viewUI);
        context.setRootDirectory(argsChecker.getDirectory());


        ProcessingStep step1 = new FindFileStep(logger);
        ProcessingStep step2 = new AskUserConfirmationStep(logger);
        ProcessingStep step3 = new CalculateRootFileNameStep(logger);
        ProcessingStep step4 = new CreateTempDirectoryStep(logger);
        ProcessingStep step5 = new ConvertPdfToJpegStep(logger);
        ProcessingStep step6 = new CopyFileStep(logger);
        ProcessingStep step7 = new CleaningStep(logger);

        step1.setNext(step2);
        step2.setNext(step3);
        step3.setNext(step4);
        step4.setNext(step5);
        step5.setNext(step6);
        step6.setNext(step7);

        boolean result = step1.handle(context);

        if (result) {
            logger.info("Process successful !");
        } else {
            logger.info("Le traitement a été interrompu.");
        }

        logger.info("PDfToJpeg done in {} ms", System.currentTimeMillis() - start);
        viewUI.showBottomRightDialogAndExit();
    }
}
