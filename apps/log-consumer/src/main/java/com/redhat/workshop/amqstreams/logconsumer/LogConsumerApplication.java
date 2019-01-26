package com.redhat.workshop.amqstreams.logconsumer;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class LogConsumerApplication {


    private List<String> lines = new CopyOnWriteArrayList<>();


    public static void main(String[] args) {
        SpringApplication.run(LogConsumerApplication.class, args);
    }

    @Bean
    public RouteBuilder routeBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                routeBuilder()
                        .restConfiguration("servlet")
                        .bindingMode(RestBindingMode.auto);


                from("kafka:{{input.topic}}")
                        .log("Received from '${in.headers[kafka.TOPIC]}': ${in.body}")
                        .process(exchange -> {
                            Message in = exchange.getIn();
                            lines.add(in.getBody(String.class));
                        });

                rest("/lines")
                        .get().route().setBody(constant(lines)).endRest();

            }
        };
    }

}

