package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import javax.swing.JOptionPane;

import models.Seed;
import models.User;

public class SeedManager {
    private Connection connection;

    public SeedManager() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the MySQL database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seed_tracker", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public void addSeed(Seed seed) {
        try {
            // Verify that the user ID exists
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, seed.getAddedBy().getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                System.err.println("User ID does not exist");
                return;
            }
    
            // Insert seed record into database
            query = "INSERT INTO Seeds (SeedType, RegistrationDate, ExpiringDate, QuantityAvailable, AddedBy) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, seed.getSeedType());
            statement.setDate(2, new java.sql.Date(seed.getRegistrationDate().getTime()));
            statement.setDate(3, new java.sql.Date(seed.getExpiringDate().getTime()));
            statement.setInt(4, seed.getQuantityAvailable());
            statement.setInt(5, UserService.getCurrentUserId());
    
            statement.executeUpdate();
    
            // Get the auto-generated id
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                System.out.println("Auto-generated ID: " + id);
            } else {
                System.out.println("Failed to retrieve auto-generated ID");
            }
        } catch (SQLException e) {
            System.err.println("Error adding seed to the database: " + e.getMessage());
        }
    }

    public List<Seed> getAllSeeds() {
        List<Seed> seeds = new ArrayList<>();
        try {
            String query = "SELECT * FROM Seeds";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("SeedID");
                String seedType = resultSet.getString("SeedType");
                Date registrationDate = resultSet.getDate("RegistrationDate");
                Date expiringDate = resultSet.getDate("ExpiringDate");
                int quantityAvailable = resultSet.getInt("QuantityAvailable");
                int addedByUserId = resultSet.getInt("AddedBy");
                User addedByUser = new User(addedByUserId, "", "", ""); // Assuming you have a User constructor that takes id, username, and password
                seeds.add(new Seed(id, seedType, registrationDate, expiringDate, quantityAvailable, addedByUser));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving seeds: " + e.getMessage());
        }
        return seeds;
    }

    public void deleteSeed(int seedID, int currentUserId) {
        try {
            String query = "DELETE FROM Seeds WHERE SeedID = ? AND AddedBy = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, seedID);
            statement.setInt(2, currentUserId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("Seed deleted successfully");
            } else {
                JOptionPane.showMessageDialog(null, "You can't delete this \n Because it wasn't added by you.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting seed: " + e.getMessage());
        }
    }

    public void updateSeed(Seed seed) {
        try {
            String query = "UPDATE Seeds SET SeedType = ?, RegistrationDate = ?, ExpiringDate = ?, QuantityAvailable = ? WHERE SeedID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, seed.getSeedType());
            statement.setDate(2, new java.sql.Date(seed.getRegistrationDate().getTime()));
            statement.setDate(3, new java.sql.Date(seed.getExpiringDate().getTime()));
            statement.setInt(4, seed.getQuantityAvailable());
            statement.setInt(5, seed.getSeedID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("Seed updated successfully");
            } else {
                System.out.println("Seed not found or update failed");
            }
        } catch (SQLException e) {
            System.err.println("Error updating seed: " + e.getMessage());
        }
    }

    public List<Seed> getExpiringSeeds(int daysBeforeExpiration) {
        List<Seed> expiringSeeds = new ArrayList<>();
    
        // Get the current date
        java.util.Date currentDate = new java.util.Date();
    
        // Calculate the expiration date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, daysBeforeExpiration);
        java.sql.Date expirationDate = new java.sql.Date(calendar.getTimeInMillis());
    
        try {
            // Retrieve seeds from the database
            String query = "SELECT * FROM Seeds WHERE ExpiringDate BETWEEN ? AND ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, new java.sql.Date(currentDate.getTime()));
            statement.setDate(2, expirationDate);
            ResultSet resultSet = statement.executeQuery();
    
            // Create Seed objects from the result set
            while (resultSet.next()) {
                int id = resultSet.getInt("SeedID");
                String seedType = resultSet.getString("SeedType");
                Date registrationDate = resultSet.getDate("RegistrationDate");
                Date expiringDate = resultSet.getDate("ExpiringDate");
                int quantityAvailable = resultSet.getInt("QuantityAvailable");
                int addedBy = resultSet.getInt("AddedBy");
                User user = getUserById(addedBy);
    
                Seed seed = new Seed(id, seedType, registrationDate, expiringDate, quantityAvailable, user);
                expiringSeeds.add(seed);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving expiring seeds: " + e.getMessage());
        }
    
        return expiringSeeds;
    }

    // Helper method to get a user by ID
    public User getUserById(int id) {
        try {
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                return new User(id, username, password, email);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            return null;
        }
    }
}


