package com.example.cameltestmockorder.route;

import com.example.cameltestmockorder.config.FooConfig;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.junit.Assert.*;

public class FooRouteTest extends CamelTestSupport {

    public static final String DIRECT_START = "direct:start";
    public static final String MOCK_HTTP_ADDRESS = "mock:http-address";
    public static final String MOCK_DLQ = "mock:dlq";

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new FooRoute(FooConfig.builder()
                .fromRoute(DIRECT_START)
                .toRoute(MOCK_HTTP_ADDRESS)
                .build());
    }

    @Test
    public void testFooRoute() throws Exception {
        context.getRouteDefinition(FooRoute.START_ROUTE_ID).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("dlq").replace().to(MOCK_DLQ);
            }
        });

        MockEndpoint dlqMock = getMockEndpoint(MOCK_DLQ);
        MockEndpoint httpMock = getMockEndpoint(MOCK_HTTP_ADDRESS);

        dlqMock.expectedMessageCount(1);
        httpMock.whenAnyExchangeReceived(exchange -> {
            throw new HttpOperationFailedException("http:somewhere:4242", 500, "Remote server failed", "", null, "");
        });

        fluentTemplate.to(DIRECT_START).request();

        assertMockEndpointsSatisfied();
    }

}