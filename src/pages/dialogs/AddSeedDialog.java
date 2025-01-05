package pages.dialogs;
// AddSeedDialog.java

import javax.swing.*;

import models.Seed;
import models.User;
import pages.MainPage;
import utils.SeedManager;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class AddSeedDialog extends JDialog {
    private JTextField seedTypeField;
    private JTextField quantityAvailableField;
    private JTextField registrationDateField;
    private JTextField expiringDateField;
    JTable table = new JTable();
    private MainPage mainPage;
    

    public AddSeedDialog(MainPage mainPage) {
        this.mainPage = mainPage;

        setModal(true);
        setTitle("Add Seed");
        setLayout(new GridBagLayout());
         setSize(600, 400);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Seed type
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("Seed Type:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        seedTypeField = new JTextField(40);
        add(seedTypeField, constraints);

        // Quantity available
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(new JLabel("Quantity Available:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        quantityAvailableField = new JTextField(40);
        add(quantityAvailableField, constraints);

        // Registration date
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(new JLabel("Registration Date(YYYY-MM--DD):"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        registrationDateField = new JTextField(40);
        add(registrationDateField, constraints);

        // Expiring date
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(new JLabel("Expiring Date(YYYY-MM--DD):"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        expiringDateField = new JTextField(40);
        add(expiringDateField, constraints);

        // Add button

        constraints.gridx = 1;
        constraints.gridy = 4;

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String seedType = seedTypeField.getText();
                int quantityAvailable = Integer.parseInt(quantityAvailableField.getText());
                Date registrationDate = Date.valueOf(registrationDateField.getText());
                Date expiringDate = Date.valueOf(expiringDateField.getText());
                 // Get the currently logged-in user
                User addedBy = getCurrentlyLoggedInUser();
                int seedID = 1;

                // Create a new Seed object
                Seed seed = new Seed(seedID, seedType, registrationDate, expiringDate, quantityAvailable, addedBy);
                // Add the seed to the database
                SeedManager seedManager = new SeedManager();
                seedManager.addSeed(seed);              

                mainPage.refreshTable();
                // Close the dialog
                dispose();
            }
        });
        add(addButton, constraints);

        pack();
        setLocationRelativeTo(null);
    }

    private User getCurrentlyLoggedInUser() {
        return User.currentUser;
    }
}
