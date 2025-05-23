import javax.swing.*;
import java.awt.*;

public class MergerWindow {

    private JFrame mergerFrame;

    /**
     * @Dashboard class allows for an interface for users to navigate system. Users can
     * add new Doctors, add a patient, view the dashboard, or log out.
     * @Author Sattyaj & Jason
     */
    public MergerWindow() {
        createDashboardUI();
    }

    /**
     * @createDashboardUI Method to create the dashboard UI
     */
    private void createDashboardUI() {
        JPanel mergerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 10, 15, 10);

        mergerFrame = new JFrame("Slide Merger");
        mergerFrame.setSize(800, 650);
        mergerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mergerFrame.add(mergerPanel, BorderLayout.CENTER);

        //slide count label
        JLabel slideCountLabel = new JLabel("Number of slides: 0");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        mergerPanel.add(slideCountLabel, constraints);

        //total number of presentations label
        JLabel totalPresentationLabel = new JLabel("Total Presentations: 0");
        constraints.gridx = 1;
        constraints.gridy = 0;
        mergerPanel.add(totalPresentationLabel, constraints);

        //explanation label
        JLabel explanationLabel = new JLabel("<html><h2>Welcome to the Slide Merger!</h2>" +
                "<p>Drag and drop your PowerPoint files here to merge them into a single presentation.</p></html>");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;  // Span across two columns
        mergerPanel.add(explanationLabel, constraints);

        //drag and drop box
        JPanel dragDropBox = new JPanel();
        dragDropBox.setPreferredSize(new Dimension(750, 400));
        dragDropBox.setBorder(BorderFactory.createTitledBorder("Drag & Drop Files Here"));
        dragDropBox.setBackground(Color.LIGHT_GRAY);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        mergerPanel.add(dragDropBox, constraints);

        //merger button
        JButton actionButton = new JButton("Merge Slides");
        constraints.gridy = 3;
        mergerPanel.add(actionButton, constraints);

        mergerFrame.setVisible(true);
    }

}
