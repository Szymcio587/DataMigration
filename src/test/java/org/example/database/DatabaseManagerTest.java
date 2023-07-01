package org.example.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class DatabaseManagerTest {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    private Connection connection;

    @Before
    public void setup() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testConnection() {
        assertNotNull("Connection should not be null", connection);
    }

    @Test
    public void testInsertData() {
        String insertQuery = "INSERT INTO users (id, name) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setInt(1, 1);
            statement.setString(2, "John Doe");

            int rowsAffected = statement.executeUpdate();
            assertEquals("One row should be affected", 1, rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRetrieveData() {
        String selectQuery = "SELECT name FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, 1);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                assertEquals("Retrieved name should match", "John Doe", name);
            } else {
                fail("No results found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}