package org.example.migration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class LegacyToCRMTransformer implements Processor {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        String legacyData = exchange.getIn().getBody(String.class);
        String[] lines = legacyData.split("\n");

        List<CRMData> crmDataList = new ArrayList<>();
        for (String line : lines) {
            String[] dataFields = line.split(",");
            String customerName = dataFields[0];
            String customerEmail = dataFields[1];
            String customerPhoneNumber = dataFields[2];
            CRMData crmData = new CRMData(customerName, customerEmail, customerPhoneNumber);
            crmDataList.add(crmData);
        }

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = objectMapper.writeValueAsString(crmDataList);
        json = json.replace("\r", "").replace("\n", "")
                .replace("\"", "").replace("\\r", "");

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
