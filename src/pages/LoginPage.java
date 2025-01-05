package pages;


import javax.swing.*;


import frames.MainFrame;
import models.User;
import utils.UserService;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserService userService;

    public LoginPage() {
        userService = new UserService();

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));

        // Create title label
        JLabel titleLabel = new JLabel("SeedTracker Login");
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

        // Create login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (userService.authenticate(username, password)) {
                    UserService service = new UserService();
                    User currentUser = service.getUserByUsername(username);
                    UserService.setCurrentUserId(currentUser.getId());
                    User.currentUser = currentUser;
                    // Login successful, navigate to main page
                    MainPage mainPage = new MainPage();
                    MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(LoginPage.this);
                    mainFrame.setCurrentPage(mainPage);
                } else {
                    // Login failed, display error message
                    JOptionPane.showMessageDialog(LoginPage.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(loginButton, getConstraints(1, 3, 1, 1));

        // Create forgot password label
        JLabel forgotPasswordLabel = new JLabel("Forgot Password?");
        forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        forgotPasswordLabel.setForeground(new Color(100, 100, 100));
        add(forgotPasswordLabel, getConstraints(1, 4, 1, 1));



        // ...
        
       // Create register link
        JButton registerLink = new JButton("Don't have an account? Register here");
        registerLink.setFont(new Font("Arial", Font.PLAIN, 14));
        registerLink.setForeground(new Color(0, 0, 255));
        registerLink.setBackground(new Color(240, 240, 240));
        registerLink.setBorder(null);
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistrationPage registrationPage = new RegistrationPage();
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(LoginPage.this);
                mainFrame.setCurrentPage(registrationPage);
            }
        });
        add(registerLink, getConstraints(1, 5, 1, 1));
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