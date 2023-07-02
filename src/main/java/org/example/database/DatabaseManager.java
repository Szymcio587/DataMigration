package org.example.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private String url;
    private String username;
    private String password;

    private String tableName, databaseName;

    public DatabaseManager() {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/database.properties")) {
            properties.load(fileInputStream);
            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.user");
            this.password = properties.getProperty("db.password");
            this.tableName = properties.getProperty("db.table");
            this.databaseName = properties.getProperty("db.source");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateAndConnect() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            CreateDatabaseIfNotExists(connection);
            connection.setCatalog(databaseName);
            CreateTable(connection);
            AddUser(connection, "Szymon", "szymontalar@gmail.com");
            RetrieveUsers(connection);
            GetTableSize(connection);
            ClearTable(connection);
            connection.close();
            System.out.println("Disconnected from the database.");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public void CreateDatabaseIfNotExists(Connection connection) {
        try  {
            String query = ReadSqlFromFile("src/main/resources/Scripts/V1/create_database.sql");
            query = query.replace("{{databaseName}}", databaseName);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Database " + databaseName + " has been created.");
        } catch (SQLException e) {
            System.out.println("Error creating the database: " + e.getMessage());
        }
    }

    public void CreateTable(Connection connection) {
        try {
            String query = ReadSqlFromFile("src/main/resources/Scripts/V1/create_table.sql");
            query = query.replace("{{tableName}}", tableName);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table " + tableName + " has been created.");
        } catch (SQLException e) {
            System.out.println("Error creating the database: " + e.getMessage());
        }
    }

    public void AddUser(Connection connection, String name, String email) {
        try {
            String query = ReadSqlFromFile("src/main/resources/Scripts/V1/add_user.sql");

            PreparedStatement preparedStatement = connection.prepareStatement(query);
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
            String query = ReadSqlFromFile("src/main/resources/Scripts/V1/retrieve_users.sql");
            query = query.replace("{{tableName}}", tableName);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

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
            String query = ReadSqlFromFile("src/main/resources/Scripts/V1/clear.sql");
            query = query.replace("{{tableName}}", tableName);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table cleared successfully.");
        } catch (SQLException e) {
            System.out.println("Error while clearing the table: " + e.getMessage());
        }
    }

    public int GetTableSize(Connection connection) {
        int records = 0;
        try {
            String query = ReadSqlFromFile("src/main/resources/Scripts/V1/table_size.sql");
            query = query.replace("{{tableName}}", tableName);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            records = resultSet.getInt("recordCount");
        } catch (SQLException e) {
            System.out.println("Error while clearing the table: " + e.getMessage());
        }
        return records;
    }

    private String ReadSqlFromFile(String filename) {
        String content = "";
        try {
            Path path = Paths.get(filename);
            content = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
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

