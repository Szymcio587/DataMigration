package org.example.database;

import java.sql.*;

public class DatabaseManager {
    private String url;
    private String username;
    private String password;

    public DatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void CreateAndConnect() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            CreateDatabaseIfNotExists(connection, "source");
            connection.setCatalog("source");
            CreateTable(connection, "users");
            System.out.println("Connected to the database.");
            AddUser(connection, "users", "Szymon", "szymontalar@gmail.com");
            RetrieveUsers(connection, "users");
            connection.close();
            System.out.println("Disconnected from the database.");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private void CreateDatabaseIfNotExists(Connection connection, String name) {
        try  {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + name + ";");
            System.out.println("Database " + name + " has been created.");
        } catch (SQLException e) {
            System.out.println("Error creating the database: " + e.getMessage());
        }
    }

    private void CreateTable(Connection connection, String name) {
        try {
            Statement statement = connection.createStatement();
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + name + " (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255)," +
                    "email VARCHAR(255)" +
                    ")";
            statement.executeUpdate(createTableQuery);
            System.out.println("Table " + name + " has been created.");
        } catch (SQLException e) {
            System.out.println("Error creating the database: " + e.getMessage());
        }
    }

    private void AddUser(Connection connection, String tableName, String name, String email) {
        try {
            String insertQuery = "INSERT INTO " + tableName + " (name, email) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        }
        catch(SQLException e) {
            System.out.println("Error executing insert query: " + e.getMessage());
        }
    }

    private void RetrieveUsers(Connection connection, String tableName) {
        try {
            String selectQuery = "SELECT * FROM " + tableName;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String column1Value = resultSet.getString("name");
                String column2Value = resultSet.getString("email");

                System.out.println("Name: " + column1Value + ", Email: " + column2Value);
            }
        } catch (SQLException e) {
            System.out.println("Error executing select query: " + e.getMessage());
        }

    }
}

