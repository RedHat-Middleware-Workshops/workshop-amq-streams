package com.redhat.workshop.amqstreams.restconsumer;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class RestConsumerApplication {


    private List<String> lines = new CopyOnWriteArrayList<>();


    public static void main(String[] args) {
        SpringApplication.run(RestConsumerApplication.class, args);
    }

    @Bean
    public RouteBuilder routeBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                routeBuilder()
                        .restConfiguration("servlet")
                        .bindingMode(RestBindingMode.auto);


                from("kafka:{{input_topic}}&&seekTo={{application.seekTo}}")
                        .log("${in.body}")
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

