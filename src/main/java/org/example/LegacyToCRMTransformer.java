package org.example;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LegacyToCRMTransformer implements Processor {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        String legacyData = exchange.getIn().getBody(String.class);
        String[] dataFields = legacyData.split(",");

        String customerName = dataFields[0];
        String customerEmail = dataFields[1];
        String customerPhoneNumber = dataFields[2];

        // Build JSON object with transformed data
        CRMData crmData = new CRMData(customerName, customerEmail, customerPhoneNumber);
        String json = objectMapper.writeValueAsString(crmData);

        exchange.getIn().setBody(json);
    }

    private static class CRMData {
        private String customerName;
        private String customerEmail;
        private String customerPhoneNumber;

        public CRMData(String customerName, String customerEmail, String customerPhoneNumber) {
            this.customerName = customerName;
            this.customerEmail = customerEmail;
            this.customerPhoneNumber = customerPhoneNumber;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        public String getCustomerPhoneNumber() {
            return customerPhoneNumber;
        }

        public void setCustomerPhoneNumber(String customerPhoneNumber) {
            this.customerPhoneNumber = customerPhoneNumber;
        }
    }
}
