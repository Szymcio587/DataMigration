package org.example.migration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class MigrationTransformer implements Processor{
    public void splitScripts(Exchange exchange) {
        Message message = exchange.getIn();
        String content = message.getBody(String.class);
        String[] scripts = content.split(";");

        for (String script : scripts) {
            script = script.trim();
            if (!script.isEmpty()) {
                message.setBody(script);
                // Set any necessary headers or properties for the script if needed
                // message.setHeader("ScriptName", <scriptName>);
                exchange.setProperty("CamelSplitComplete", null);
                exchange.setProperty("CamelSplitIndex", null);
                exchange.setProperty("CamelSplitSize", null);
                // Process each script individually
                try {
                    this.process(exchange);
                } catch (Exception e) {
                    // Handle any exceptions that occur during processing
                    // You can choose to continue processing or terminate the route
                }
            }
        }
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        splitScripts(exchange);
    }
}
