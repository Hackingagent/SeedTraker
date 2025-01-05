package frames;

import javax.swing.*;

import pages.MainPage;

import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;

    public MainFrame() {
        // Set up frame
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("SeedTracker");

        // Create main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Add main panel to frame
        add(mainPanel);
    }

    // Method to set the current page
    public void setCurrentPage(JPanel page) {
        Container container = getContentPane();
        container.removeAll();
        container.add(page);
        container.revalidate();
        container.repaint();
    }

    private MainPage currentPage;


    public MainPage getCurrentPage() {
        return currentPage;
    }

}