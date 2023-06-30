package org.example;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class MainApp {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new MyRouteBuilder());
        context.start();
        Thread.sleep(5000); // Wait for the migration process to complete
        context.stop();
    }
}
