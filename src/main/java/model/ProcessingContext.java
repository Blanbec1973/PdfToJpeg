package model;


import view.ViewUI;

import java.io.File;

public class ProcessingContext {
    private File mostRecentFile;
    private String rootFileName;
    private File tempDir;
    private File rootDirectory;
    private final ViewUI viewUI;

    public ProcessingContext(ViewUI viewUI) {
        this.viewUI = viewUI;
    }

    public void setMostRecentFile(File file) {
        mostRecentFile=file;
    }

    public void setTempDir(File tempDir) {
        this.tempDir = tempDir;
    }

    public void setRootDirectory(String directory) {
        rootDirectory = new File(directory);
    }

    public void setRootFileName(String rootFileName) {
        this.rootFileName = rootFileName;
    }

    public String getRootFileName() {
        return rootFileName;
    }

    public File getRootDirectory() {
        return this.rootDirectory;
    }

    public File getTempDir() {
        return this.tempDir;
    }

    public File getMostRecentFile() {
        return mostRecentFile;
    }

    public ViewUI getUI() {
        return viewUI;
    }
}

