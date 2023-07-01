package org.example.database;

import org.junit.Before;
import org.junit.Test;
import java.sql.*;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    private DatabaseManager databaseManager;

    @Before
    public void setup() {
        databaseManager = new DatabaseManager();
    }

    @Test
    public void TestConnectionAndQueries() {
        int recordsNumber = 0;
        try (Connection connection = DriverManager.getConnection(databaseManager.getUrl(),
                databaseManager.getUsername(), databaseManager.getPassword())) {
            databaseManager.CreateDatabaseIfNotExists(connection);
            connection.setCatalog(databaseManager.getDatabaseName());
            databaseManager.CreateTable(connection);
            System.out.println("Connected to the database.");
            databaseManager.AddUser(connection, "Szymon", "szymontalar@gmail.com");
            databaseManager.AddUser(connection, "Jan", "asdasz@gmail.com");
            databaseManager.AddUser(connection, "Marcinek", "widmotk@gmail.com");
            recordsNumber = databaseManager.GetTableSize(connection);
            databaseManager.RetrieveUsers(connection);
            databaseManager.ClearTable(connection);
            connection.close();
            System.out.println("Disconnected from the database.");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        assertEquals(recordsNumber, 3);
    }

}