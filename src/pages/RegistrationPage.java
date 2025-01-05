package pages;

import javax.swing.*;

import frames.MainFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import utils.UserService;

public class RegistrationPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private UserService userService;

    public RegistrationPage() {
        userService = new UserService();

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));

        // Create title label
        JLabel titleLabel = new JLabel("SeedTracker Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, getConstraints(0, 0, 2, 1));

        // Create username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(usernameLabel, getConstraints(0, 1, 1, 1));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(usernameField, getConstraints(1, 1, 1, 1));

        // Create password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(passwordLabel, getConstraints(0, 2, 1, 1));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(passwordField, getConstraints(1, 2, 1, 1));

        // Create confirm password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(confirmPasswordLabel, getConstraints(0, 3, 1, 1));
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(confirmPasswordField, getConstraints(1, 3, 1, 1));

        // Create register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
        
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                if (userService.registerUser(username, password)) {
                    JOptionPane.showMessageDialog(null, "User registered successfully!");
                    // Go back to login page
                    LoginPage loginPage = new LoginPage();
                    MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(RegistrationPage.this);
                    mainFrame.setCurrentPage(loginPage);
                } else {
                    JOptionPane.showMessageDialog(null, "Username already taken", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(registerButton, getConstraints(1, 4, 1, 1));

        // Create login link
        JLabel loginLink = new JLabel("Already have an account? Login here");
        loginLink.setFont(new Font("Arial", Font.PLAIN, 14));
        loginLink.setForeground(new Color(0, 0, 255));
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                LoginPage loginPage = new LoginPage();
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(RegistrationPage.this);
                mainFrame.setCurrentPage(loginPage);
            }
        });
        add(loginLink, getConstraints(1, 5, 1, 1));
    }

    // Helper method to get GridBagConstraints
    private GridBagConstraints getConstraints(int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.insets = new Insets(5, 5, 5, 5);
        return constraints;
    }
}