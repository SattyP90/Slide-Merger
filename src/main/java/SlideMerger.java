import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SlideMerger {

    private final List<File> inputFiles = new ArrayList<>();
    private final List<File> pdfsToMerge = new ArrayList<>();

    public void addFile(File file) throws IOException {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".pdf")) {
            pdfsToMerge.add(file);
        } else if (name.endsWith(".pptx")) {
            File converted = convertPptxToPdf(file);
            pdfsToMerge.add(converted);
        }
        inputFiles.add(file);
    }

    public int getPresentationCount() {
        return inputFiles.size();
    }

    public int getTotalSlideCount() throws IOException {
        int count = 0;
        for (File pdf : pdfsToMerge) {
            try (PDDocument doc = PDDocument.load(pdf)) {
                count += doc.getNumberOfPages();
            }
        }
        return count;
    }

    public File mergeToPdf(File output) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDestinationFileName(output.getAbsolutePath());
        for (File pdf : pdfsToMerge) {
            merger.addSource(pdf);
        }
        merger.mergeDocuments(null);
        return output;
    }

    private File convertPptxToPdf(File pptxFile) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(Files.newInputStream(pptxFile.toPath()));
        Dimension pageSize = ppt.getPageSize();
        List<XSLFSlide> slides = ppt.getSlides();

        PDDocument pdfDoc = new PDDocument();
        for (XSLFSlide slide : slides) {
            BufferedImage img = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();

            // Clear background white
            graphics.setPaint(Color.WHITE);
            graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width, pageSize.height));

            // Render slide
            slide.draw(graphics);

            // Add to PDF
            File tempImgFile = File.createTempFile("slide", ".png");
            ImageIO.write(img, "png", tempImgFile);

            BufferedImage buffered = ImageIO.read(tempImgFile);
            PDDocument tempDoc = new PDDocument();
            pdfDoc.addPage(PDDocument.load(tempImgFile).getPage(0));
            tempImgFile.deleteOnExit();
        }

        File outputPdf = File.createTempFile("converted_", ".pdf");
        pdfDoc.save(outputPdf);
        pdfDoc.close();
        return outputPdf;
    }
}
