package org.example;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.example.database.DatabaseManager;
import org.example.migration.MigrationManager;
import org.example.migration.MyRouteBuilder;

public class MainApp {

    private static final  DatabaseManager databaseManager = new DatabaseManager();
    private static final MigrationManager migrationManager = new MigrationManager();
    public static void main(String[] args) throws Exception {
        databaseManager.CreateAndConnect();
        //migrationManager.Migrate();
    }
}
