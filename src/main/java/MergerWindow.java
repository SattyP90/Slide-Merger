import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergerWindow {

    private JFrame mergerFrame;
    private JLabel slideCountLabel;
    private JLabel totalPresentationLabel;
    private final SlideMerger slideMerger = new SlideMerger();

    public MergerWindow() {
        createDashboardUI();
    }

    private void createDashboardUI() {
        JPanel mergerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 10, 15, 10);

        mergerFrame = new JFrame("Slide Merger");
        mergerFrame.setSize(800, 650);
        mergerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mergerFrame.add(mergerPanel, BorderLayout.CENTER);

        // Slide count label
        slideCountLabel = new JLabel("Number of slides: 0");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        mergerPanel.add(slideCountLabel, constraints);

        // Total presentations label
        totalPresentationLabel = new JLabel("Total Presentations: 0");
        constraints.gridx = 1;
        constraints.gridy = 0;
        mergerPanel.add(totalPresentationLabel, constraints);

        // Explanation Label
        JLabel explanationLabel = new JLabel("<html><h2>Welcome to the Slide Merger!</h2>" +
                "<p>Drag and drop your PowerPoint or PDF files here to merge them into a single presentation.</p></html>");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        mergerPanel.add(explanationLabel, constraints);

        // Drag and Drop Box
        JPanel dragDropBox = new JPanel();
        dragDropBox.setPreferredSize(new Dimension(750, 300));
        dragDropBox.setBorder(BorderFactory.createTitledBorder("Drag & Drop Files Here"));
        dragDropBox.setBackground(Color.LIGHT_GRAY);

        dragDropBox.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    List<File> droppedFiles = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        slideMerger.addFile(file);
                    }
                    updateCounts();
                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });

        constraints.gridy = 2;
        mergerPanel.add(dragDropBox, constraints);

        // Merge Button
        JButton actionButton = new JButton("Merge Slides");
        actionButton.addActionListener(e -> {
            try {
                slideMerger.mergeSlides("MergedOutput");
                updateCounts();
                JOptionPane.showMessageDialog(mergerFrame, "Slides merged and saved in the current directory.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mergerFrame, "Merging failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        constraints.gridy = 3;
        mergerPanel.add(actionButton, constraints);

        mergerFrame.setVisible(true);
    }

    private void updateCounts() {
        slideCountLabel.setText("Number of slides: " + slideMerger.getTotalSlides());
        totalPresentationLabel.setText("Total Presentations: " + slideMerger.getTotalPresentations());
    }


}
