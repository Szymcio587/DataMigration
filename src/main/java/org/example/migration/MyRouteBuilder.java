package org.example.migration;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:src/main/resources/LegacySystemData")
                .routeId("migrationRoute")
                .onException(Exception.class)
                .handled(true)
                .to("log:error")
                .end()
                .split().tokenize("\n")
                .to("jdbc:sourceDB")
                .to("jdbc:targetDB")
                .end();
    }
}

