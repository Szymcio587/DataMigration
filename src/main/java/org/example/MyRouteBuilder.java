package org.example;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:src/main/resources/LegacySystemData")
                .convertBodyTo(String.class)
                .split().tokenize("\n")
                .process(new LegacyToCRMTransformer())
                .to("file:src/main/resources/CRMSystemData");
    }
}

