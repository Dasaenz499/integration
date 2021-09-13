package org.company.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.company.model.User;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component
public class RestConsumer extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration().apiContextRouteId("route-api")
                .component("servlet")
                .bindingMode(RestBindingMode.auto)
                .enableCORS(true)
                .apiProperty("base.path", "/mediosDePago")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
                .apiProperty("api.title", "Test")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .clientRequestValidation(true);

        rest("user")
            .get()
                .to("direct:getUsers")
            .post()
                .type(User.class)
                .consumes(MediaType.APPLICATION_JSON)
                .to("direct:saveUser");

    }
}
