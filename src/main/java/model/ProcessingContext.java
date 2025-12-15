package model;


import view.IUserInterface;

import java.io.File;

public class ProcessingContext {
    private File mostRecentFile;
    private String rootFileName;
    private File tempDir;
    private File rootDirectory;
    private final IUserInterface viewUI;

    public ProcessingContext(IUserInterface viewUI) {
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

    public IUserInterface getUI() {
        return viewUI;
    }
}

