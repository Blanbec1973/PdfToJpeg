package model;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import java.io.File;
import java.io.IOException;

public interface IDocumentPDF {

    public boolean convertPdfToJpeg(String rootFileName, File tempDir) throws IOException;
    public void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException;

}
