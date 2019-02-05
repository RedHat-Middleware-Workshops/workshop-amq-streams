package com.redhat.workshop.amqstreams.timerproducer;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TimerProducerApplication {

    @Bean
    public RouteBuilder routeBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {


                from("timer:hello?period={{timer.period}}")
                        .transform().simple("Message ${in.header.CamelTimerCounter} at ${in.header.CamelTimerFiredTime}")
                        .log("Sent ${in.body}")
                        .inOnly("kafka:{{output.topic}}");


            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(TimerProducerApplication.class, args);
    }

}

