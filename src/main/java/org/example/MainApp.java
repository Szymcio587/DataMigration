package org.example;

import org.example.database.DatabaseManager;
import org.example.migration.MigrationManager;

public class MainApp {

    private static final  DatabaseManager databaseManager = new DatabaseManager();
    private static final MigrationManager migrationManager = new MigrationManager();
    public static void main(String[] args) throws Exception {
        //databaseManager.CreateSourceAndConnect();
        migrationManager.Migrate();
    }
}
