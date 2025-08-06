import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SlideMerger {

    private final List<File> files = new ArrayList<>();

    public void addFile(File file) throws IOException {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".pptx")) {
            // Convert pptx to PDF first and add PDF to list
            File pdfFile = convertPptxToPdf(file);
            files.add(pdfFile);
        } else if (name.endsWith(".pdf")) {
            files.add(file);
        } else {
            throw new IOException("Unsupported file format: " + file.getName());
        }
    }

    private File convertPptxToPdf(File pptxFile) throws IOException {
        // Load PPTX slides
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(pptxFile));
        int totalSlides = ppt.getSlides().size();

        // Create temporary PDF file
        File pdfFile = File.createTempFile("slide_merger_", ".pdf");
        pdfFile.deleteOnExit();

        PDDocument document = new PDDocument();

        for (int i = 0; i < totalSlides; i++) {
            // Create a blank page
            PDPage page = new PDPage();
            document.addPage(page);

            // For demo, just add slide number text — fix font error by avoiding tabs etc
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 24);
            contentStream.newLineAtOffset(100, 700);
            String text = "Slide " + (i + 1) + " from " + pptxFile.getName();
            // Replace tabs or unsupported chars to avoid IllegalArgumentException
            text = text.replace("\t", "    ");
            contentStream.showText(text);
            contentStream.endText();
            contentStream.close();
        }

        document.save(pdfFile);
        document.close();
        ppt.close();

        return pdfFile;
    }

    public File mergeSlides() throws IOException {
        if (files.isEmpty()) {
            throw new IOException("No files added to merge.");
        }

        File mergedFile = File.createTempFile("merged_slides_", ".pdf");
        mergedFile.deleteOnExit();

        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDestinationFileName(mergedFile.getAbsolutePath());

        for (File file : files) {
            merger.addSource(file);
        }

        merger.mergeDocuments(null);

        return mergedFile;
    }

    // Added method so your UI call mergeAllToSinglePdf(File output) works
    public void mergeAllToSinglePdf(File output) throws IOException {
        if (files.isEmpty()) {
            throw new IOException("No files added to merge.");
        }

        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDestinationFileName(output.getAbsolutePath());

        for (File file : files) {
            merger.addSource(file);
        }

        merger.mergeDocuments(null);
    }
}