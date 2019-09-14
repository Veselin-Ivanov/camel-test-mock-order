package com.example.cameltestmockorder.route;

import com.example.cameltestmockorder.config.FooConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class FooRoute extends RouteBuilder {

    public static final String ANY_EXCEPTION_ROUTE_ID = "any-exception";
    public static final String START_ROUTE_ID = "start";

    private FooConfig config;

    @Autowired
    public FooRoute(FooConfig config){
        this.config = config;
    }

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .routeId(ANY_EXCEPTION_ROUTE_ID)
                .to("activemq:dlq").id("dlq")
                .handled(true)
                ;

        from(config.getFromRoute())
                .routeId(START_ROUTE_ID)
                .process(exchange -> {
                    //what ever process
                    log.info("Things has happened");
                })
                .to(config.getToRoute())
        ;
    }
}
