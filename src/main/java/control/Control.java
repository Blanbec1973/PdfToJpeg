package control;

import model.ProcessingContext;
import model.steps.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.heyner.common.Parameter;
import view.ViewUI;
import view.ViewUIAppender;

import java.io.IOException;



public class Control {

    public static void main(String[] args) throws IOException {
        new Control(args);
    }

    public Control(String[] args) {
        long start = System.currentTimeMillis();
        ViewUI viewUI = new ViewUI();
        // Récupère le logger racine en tant que 'core.Logger'
        Logger logger = (Logger) LogManager.getRootLogger();

        ViewUIAppender appender = ViewUIAppender.createAppender();
        appender.start(); // <-- Démarre l'appender
        logger.addAppender(appender);
        logger.setAdditive(true); // Pour garder les autres appenders (console, fichier)

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
            System.out.println("Traitement terminé avec succès !");
            // Affichage final, suppression du répertoire temporaire, etc.
        } else {
            System.out.println("Le traitement a été interrompu.");
        }


        /*boolean continueProgram = myFileUtils.findFileToProcess();


        if (!myFileUtils.findFileToProcess()) {
            logger.info("No file to process.");
            return;
        }

        if (!myFileUtils.calculateRootFileName(myFileUtils.getMostRecentFile())) {
            logger.info("Nom de fichier cible incorrect.");
            return;
        }





        if (continueProgram) {
            myFileUtils.copyDirectoryTemp();
        }
*/
        logger.info("PDfToJpeg done in {} ms", System.currentTimeMillis() - start);
        viewUI.showBottomRightDialogAndExit();
    }
}
