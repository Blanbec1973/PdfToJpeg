package model;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class MyFileUtils {
    private static final Logger logger = LogManager.getLogger(MyFileUtils.class);
    private File rootDirectory = null;
    private String rootFileName = null;
    private File tempDir = null;
    private File mostRecentFile = null;

    public File getRootDirectory() {return rootDirectory;}
    public void setRootDirectory(String rootDirectory) {this.rootDirectory = new File(rootDirectory);}
    public String getRootFileName() {
        return rootFileName;
    }
    public File getTempDir() {return tempDir;}
    public File getMostRecentFile() {return mostRecentFile;}

    public boolean calculateRootFileName(File mostRecentFile) {
        logger.info("Calculate target file name.");
        String tempName = mostRecentFile.getName();
        int first = tempName.indexOf("-")+1;
        int end = tempName.indexOf("-",first);
        String temp = tempName.substring(first, end);
        int fileNumber = Integer.parseInt(temp.trim());

        if (fileNumber == 12 || fileNumber == 22 || fileNumber == 32) {
            String tempDest1 = tempName.replace("- 12 -","- 13 -");
            String tempDest2 = tempDest1.replace("- 22 -", "- 23 -");
            String tempDest3 = tempDest2.replace("- 32 -","- 33 -");
            rootFileName = tempDest3.replace(".pdf", "");
            logger.info ("Target file name : {}",rootFileName);
            return true ;
        }
        logger.info("Bad file number, expected 12 or 22, founded : {}", fileNumber);
        return false ;
    }
    public void createTempDirectory(String pathTemp) {
        tempDir = new File(pathTemp);
        tempDir.mkdirs();
        logger.info("Temp directory : {}.", tempDir.getAbsolutePath());
    }

    public void copyDirectoryTemp() throws IOException {
        FileUtils.copyDirectory(tempDir, rootDirectory);
        logger.info("Copying files from {} to {}", tempDir, rootDirectory);
    }
    public boolean findFileToProcess() {
        logger.info("Try to find file in {}.", rootDirectory);

        File[] files = rootDirectory.listFiles((dir, name) -> name.endsWith(".pdf"));

        long mostRecentTimestamp = Long.MIN_VALUE;

        for (File file : files) {
            long lastModifiedTimestamp = file.lastModified();
            if (lastModifiedTimestamp > mostRecentTimestamp) {
                mostRecentFile = file;
                mostRecentTimestamp = lastModifiedTimestamp;
            }
        }

        if (mostRecentFile == null) {
            logger.info("No file founded.");
            return false;
        }

        logger.info("File found : {}", mostRecentFile.getAbsolutePath());
        return true;
    }
}
