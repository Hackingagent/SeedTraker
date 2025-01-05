package pages;


import javax.swing.*;

import java.awt.event.MouseEvent;
import frames.MainFrame;
import java.util.List;
import models.Seed;
import models.SeedTableModel;
import pages.dialogs.AddSeedDialog;
import pages.dialogs.EditSeedDialog;
import utils.SeedManager;
import utils.UserService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class MainPage extends JPanel {
    private JTable table;
    private SeedManager seedService;
    public MainPage() {
        setLayout(new BorderLayout());

        // Create a title label
        JLabel titleLabel = new JLabel("Seed Tracker Main Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Create a content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        add(contentPanel, BorderLayout.CENTER);

        JButton addSeedButton = new JButton("Add Seed");
        addSeedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Display the Add Seed dialog
                    // Display the Add Seed dialog
                AddSeedDialog dialog = new AddSeedDialog(MainPage.this);
                dialog.setVisible(true);
            }
        });
        contentPanel.add(addSeedButton);

        
        seedService = new SeedManager();
        List<Seed> seeds = seedService.getAllSeeds();
        UserService userService = new UserService(); // Create UserService instance
        SeedTableModel tableModel = new SeedTableModel(seeds, userService); // Pass UserService instance to SeedTableModels
        table = new JTable(tableModel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if (column == 6) {
                    // Edit button clicked
                    Seed seed = seeds.get(row);
                    EditSeedDialog dialog = new EditSeedDialog(MainPage.this, seed);
                    dialog.setVisible(true);
                    // Open edit dialog
                } else if (column == 7) {
                    // Delete button clicked
                    int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this seed?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        Seed seed = seeds.get(row);
                        SeedManager seedManager = new SeedManager();
                        int currentUserId = UserService.getCurrentUserId(); // Assuming you have a method to get the current user ID
                        seedManager.deleteSeed(seed.getSeedID(), currentUserId);
                        refreshTable();
                    }
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add scroll pane to frame
        contentPanel.add(scrollPane);
    



        // Create logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    LoginPage loginPage = new LoginPage();
                    MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(MainPage.this);
                    mainFrame.setCurrentPage(loginPage);
                }
            }
        });
        add(logoutButton, BorderLayout.SOUTH);  
    }

    public void refreshTable() {
        seedService = new SeedManager();
        List<Seed> seeds = seedService.getAllSeeds();
        UserService userService = new UserService(); 
        SeedTableModel tableModel = new SeedTableModel(seeds, userService); 
        table.setModel(tableModel);
        table.revalidate();
        table.repaint();
    }

    
}