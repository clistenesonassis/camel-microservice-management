package com.example.microservices.camelmicrcoservicesa.router;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyTimerRouter extends RouteBuilder {
    public static final String ROUTE_URI_TEST = "direct:test-com.example.microservices.camelmicrcoservicesb.router";

    @Autowired
    GetCurrentTime getCurrentTime;

    @Autowired
    SimpleLoggingProcessingComponent simpleLoggingProcessingComponent;

    @Override
    public void configure() throws Exception {

        from("direct:my-timer")
                .log("${body}")
                .transform().constant("testando aplicação. :P")
                .log("${body}")
                .bean(getCurrentTime, "getCurrentTime")
                .log("${body}")
                .bean(simpleLoggingProcessingComponent)
                .log("${body}")
                .process(new SimpleLoggingProcess())
                .log("${body}")
                .to(ROUTE_URI_TEST)
                .log("${exchange}")
                .end();

        from(ROUTE_URI_TEST)
                .log("${exchangeProperty[status]}")
                .end();
    }
}

@Component
class GetCurrentTime {

    public String getCurrentTime() {
        return "Time now is - " + LocalDateTime.now();
    }
}

@Component
class SimpleLoggingProcessingComponent {
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

    public void process(String msg) {
        logger.info("SimpleLoggingProcessingComponent {}", msg);
    }
}

class SimpleLoggingProcess implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.setProperty("status", "Functioned");
        exchange.getMessage().setBody("Alterei mesmo sem ter como!! :P");
    }
}
