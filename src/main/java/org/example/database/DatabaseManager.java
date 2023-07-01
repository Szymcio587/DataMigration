package org.example.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private String url;
    private String username;
    private String password;

    private final String tableName = "users", databaseName = "source";

    public DatabaseManager() {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/database.properties")) {
            properties.load(fileInputStream);
            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.user");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateAndConnect() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            CreateDatabaseIfNotExists(connection);
            connection.setCatalog(databaseName);
            CreateTable(connection);
            System.out.println("Connected to the database.");
            AddUser(connection, "Szymon", "szymontalar@gmail.com");
            RetrieveUsers(connection);
            ClearTable(connection);
            connection.close();
            System.out.println("Disconnected from the database.");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public void CreateDatabaseIfNotExists(Connection connection) {
        try  {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName + ";");
            System.out.println("Database " + databaseName + " has been created.");
        } catch (SQLException e) {
            System.out.println("Error creating the database: " + e.getMessage());
        }
    }

    public void CreateTable(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255)," +
                    "email VARCHAR(255)" +
                    ")";
            statement.executeUpdate(createTableQuery);
            System.out.println("Table " + tableName + " has been created.");
        } catch (SQLException e) {
            System.out.println("Error creating the database: " + e.getMessage());
        }
    }

    public void AddUser(Connection connection, String name, String email) {
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

    public void RetrieveUsers(Connection connection) {
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

    public void ClearTable(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + tableName);
            System.out.println("Table cleared successfully.");
        } catch (SQLException e) {
            System.out.println("Error while clearing the table: " + e.getMessage());
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}

