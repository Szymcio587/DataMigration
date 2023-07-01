package org.example.migration;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class MigrationManager {

    public void Migrate() throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new MyRouteBuilder());
        context.start();
        Thread.sleep(5000);
        context.stop();
    }
}
