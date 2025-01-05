package utils;

import org.mindrot.jbcrypt.BCrypt;

import models.User;

import java.sql.*;

public class UserService {
    private Connection connection;

    private static int currentUserId;

    public static void setCurrentUserId(int id) {
        currentUserId = id;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    

    public UserService() {
        try {
            // Load the MySQL Connector/J driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seed_tracker", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public boolean authenticate(String username, String password) {
        if (connection == null) {
            throw new RuntimeException("Database connection is null");
        }
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Please fill in all fields!");
            return false;
        }
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            System.out.println("Attempting to authenticate user: " + username);
            // Prepare a query to retrieve the user
            statement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            statement.setString(1, username);

            // Execute the query and retrieve the result
            result = statement.executeQuery();
            System.out.println("Query executed. Checking results...");

            // Check if the user exists
            if (result.next()) {
                System.out.println("User found!");
                // Retrieve the stored hashed password
                String storedHashedPassword = result.getString("password");
                // Check if the provided password matches the stored hashed password
                return BCrypt.checkpw(password, storedHashedPassword);
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        } finally {
            // Close database resources
            try {
                if (statement != null) {
                    statement.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean registerUser(String username, String email, String password) {
        if (connection == null) {
            throw new RuntimeException("Database connection is null");
        }
        PreparedStatement statement = null;
        try {
            // Check if the username or email already exists
            statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? OR email = ?");
            statement.setString(1, username);
            statement.setString(2, email);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                System.out.println("Username or email already exists!");
                return false;
            }
    
            // Hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    
            // Prepare a query to insert the new user
            statement = connection.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, hashedPassword);
    
            // Execute the query
            statement.executeUpdate();
            System.out.println("User registered successfully!");
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        } finally {
            // Close database resources
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    }

     public User getUserByUsername(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                return new User(id, username, password, email);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
            return null;
        }
    }

    public Object getUsernameById(int id) {
        try {
            String query = "SELECT username FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving username: " + e.getMessage());
            return null;
        }
    } 
    
}