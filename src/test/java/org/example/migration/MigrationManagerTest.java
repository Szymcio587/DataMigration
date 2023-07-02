package org.example.migration;

import org.example.database.DatabaseManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MigrationManagerTest {

    private MigrationManager migrationManager;

    @Before
    public void setup() {
        migrationManager = new MigrationManager();
    }

    @Test
    public void TestMigration() throws Exception {
        migrationManager.Migrate();
    }
}