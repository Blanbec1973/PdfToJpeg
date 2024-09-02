package model;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import java.io.File;
import java.io.IOException;

public interface IDocumentPDF {

    boolean convertPdfToJpeg(String rootFileName, File tempDir) throws IOException;
    void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException;

}
