package org.example.migration;

import org.apache.camel.builder.RouteBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyRouteBuilder extends RouteBuilder {

    private Properties properties;

    public void ReadProperties() {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/database.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure() throws Exception {
        ReadProperties();

        from("file:src/main/resources/Scripts/V1")
                .routeId("route1")
                .onException(Exception.class)
                .handled(true)
                .to("log:error")
                .end()
                .split().tokenize("\n")
                .to(properties.getProperty("db.url").concat(properties.getProperty("db.source")))
                .to(properties.getProperty("db.url").concat(properties.getProperty("db.target")))
                .end();
    }
}

