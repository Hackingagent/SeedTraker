package pages.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.*;

import models.Seed;
import pages.MainPage;  
import utils.SeedManager;
import utils.UserService;

public class EditSeedDialog extends JDialog {
    private Seed seed;
    private MainPage mainPage;

    public EditSeedDialog(MainPage mainPage, Seed seed) {
        setModal(true);
        setTitle("Edit Seed");

        // Check if the current user is the one who added the seed
        int currentUserId = UserService.getCurrentUserId();
        if (seed.getAddedBy().getId() != currentUserId) {
            JOptionPane.showMessageDialog(null, "You are not authorized to edit this seed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create dialog fields
        JLabel seedTypeLabel = new JLabel("Seed Type:");
        JTextField seedTypeField = new JTextField(seed.getSeedType());

        JLabel registrationDateLabel = new JLabel("Registration Date:");
        JTextField registrationDateField = new JTextField(seed.getRegistrationDate().toString());

        JLabel expiringDateLabel = new JLabel("Expiring Date:");
        JTextField expiringDateField = new JTextField(seed.getExpiringDate().toString());

        JLabel quantityAvailableLabel = new JLabel("Quantity Available:");
        JTextField quantityAvailableField = new JTextField(String.valueOf(seed.getQuantityAvailable()));

        // Create save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update seed data
                seed.setSeedType(seedTypeField.getText());
                seed.setRegistrationDate(Date.valueOf(registrationDateField.getText()));
                seed.setExpiringDate(Date.valueOf(expiringDateField.getText()));
                seed.setQuantityAvailable(Integer.parseInt(quantityAvailableField.getText()));

                // Update seed data in database
                SeedManager seedManager = new SeedManager();
                seedManager.updateSeed(seed);

                // Refresh table
                mainPage.refreshTable();

                // Close dialog
                dispose();
            }
        });

        // Layout dialog components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(seedTypeLabel);
        panel.add(seedTypeField);
        panel.add(registrationDateLabel);
        panel.add(registrationDateField);
        panel.add(expiringDateLabel);
        panel.add(expiringDateField);
        panel.add(quantityAvailableLabel);
        panel.add(quantityAvailableField);
        panel.add(saveButton);

        add(panel);
        pack();
        
    }
}