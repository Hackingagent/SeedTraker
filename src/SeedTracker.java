import frames.MainFrame;
import pages.LoginPage;

public class SeedTracker {
     public static void main(String[] args) {
        // Create a new main frame
        MainFrame mainFrame = new MainFrame();

        // Set the login page as the current page
        LoginPage loginPage = new LoginPage();
        mainFrame.setCurrentPage(loginPage);

        // Make the main frame visible
        mainFrame.setVisible(true);
    }
}
