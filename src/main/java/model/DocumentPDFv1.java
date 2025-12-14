package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DocumentPDFv1 implements IDocumentPDF{
    private static final Logger logger = LogManager.getRootLogger();
    public static final String JAVAX_IMAGEIO_JPEG_IMAGE_1_0 = "javax_imageio_jpeg_image_1.0";
    private final PDDocument pdfDocument;
    public static final String VALUE = "value";
    public static final String DENSITY_UNITS_PIXELS_PER_INCH = "01";


    public DocumentPDFv1(File mostRecentFile) throws IOException {
        pdfDocument = Loader.loadPDF(mostRecentFile);
    }
    @Override
    public boolean convertPdfToJpeg(String rootFileName, File tempDir) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);

        for (int page = 0; page < pdfDocument.getNumberOfPages(); page++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 200, ImageType.RGB);
            String outputFileName = rootFileName + "_page" + (page + 1) + ".jpg";

            final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }

            setDPI2(metadata);

            final File file = new File(tempDir, outputFileName);
            logger.info("Tentative de création du fichier : {}", file.getAbsolutePath());
            try (ImageOutputStream stream = ImageIO.createImageOutputStream(file)) {
                if (stream == null) {
                    throw new IOException("Impossible de créer le flux de sortie pour " + file.getAbsolutePath());
                }
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
            } catch (RuntimeException e) {
                logger.error("Erreur lors de la création du fichier : {}", e.getMessage(), e);
                javax.swing.JOptionPane.showMessageDialog(null, "Erreur : " + e.getMessage());
            }

            logger.info("Creating file {}.", file.getAbsolutePath());
        }

        return true;
    }

    @Override
        public void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException {
            // for JFIF, it's dots per inch
            double dotsPerInch = 150;

            IIOMetadataNode horizontal = new IIOMetadataNode("HorizontalPixelSize");
            horizontal.setAttribute(VALUE, Double.toString(dotsPerInch));

            IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
            vert.setAttribute(VALUE, Double.toString(dotsPerInch));

            IIOMetadataNode dim = new IIOMetadataNode("Dimension");
            dim.appendChild(horizontal);
            dim.appendChild(vert);

            IIOMetadataNode jpegVariety = new IIOMetadataNode("JPEGvariety");
            IIOMetadataNode markerSequence = new IIOMetadataNode("markerSequence");
            IIOMetadataNode sof = new IIOMetadataNode("sof");

            IIOMetadataNode root = new IIOMetadataNode(JAVAX_IMAGEIO_JPEG_IMAGE_1_0);
            root.appendChild(dim);
            root.appendChild(jpegVariety);
            root.appendChild(markerSequence);
            printChildren(root, "");
            root.appendChild(sof);

            metadata.mergeTree(JAVAX_IMAGEIO_JPEG_IMAGE_1_0, root);
    }

    private static void setDPI2(IIOMetadata metadata) throws IIOInvalidTreeException {
        String metadataFormat = JAVAX_IMAGEIO_JPEG_IMAGE_1_0;
        IIOMetadataNode root = new IIOMetadataNode(metadataFormat);
        IIOMetadataNode jpegVariety = new IIOMetadataNode("JPEGvariety");
        IIOMetadataNode markerSequence = new IIOMetadataNode("markerSequence");

        IIOMetadataNode app0JFIF = new IIOMetadataNode("app0JFIF");
        app0JFIF.setAttribute("majorVersion", "1");
        app0JFIF.setAttribute("minorVersion", "2");
        app0JFIF.setAttribute("thumbWidth", "0");
        app0JFIF.setAttribute("thumbHeight", "0");
        app0JFIF.setAttribute("resUnits", DENSITY_UNITS_PIXELS_PER_INCH);
        app0JFIF.setAttribute("Xdensity", String.valueOf(150));
        app0JFIF.setAttribute("Ydensity", String.valueOf(150));

        root.appendChild(jpegVariety);
        root.appendChild(markerSequence);
        jpegVariety.appendChild(app0JFIF);
        printChildren(root, "");

        metadata.mergeTree(metadataFormat, root);
    }

    private static void printChildren(IIOMetadataNode node, String indent) {
        String nodeName = node.getNodeName();
        indent += '/' + nodeName;

        final NodeList childNodes = node.getChildNodes();
        int childCount = childNodes.getLength();

        for (int n = 0; n < childCount; ++n) {
            IIOMetadataNode child = (IIOMetadataNode) childNodes.item(n);
            String childName = child.getNodeName();

            NamedNodeMap attributes = child.getAttributes();
            int aLength = attributes.getLength();

            for (int a = 0; a < aLength; ++a) {
                Node item = attributes.item(a);
                final String itemName = item.getNodeName();
                if (logger.isInfoEnabled())
                    logger.info(" [{}/{}] {} = {}", indent, childName, itemName, child.getAttribute(itemName));
            }

            try {
                printChildren(child, indent);
            } catch (Exception e) {
                logger.error("Erreur lors de l'affichage des métadonnées : {}", e.getMessage());
            }

        }
    }




}
