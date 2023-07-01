package org.example.migration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.example.migration.LegacyToCRMTransformer;

public class MyRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:src/main/resources/LegacySystemData")
                .convertBodyTo(String.class)
                .process(new LegacyToCRMTransformer())
                .marshal().json(JsonLibrary.Jackson)
                .to("file:src/main/resources/CRMSystemData?fileName=data.json");
    }
}

