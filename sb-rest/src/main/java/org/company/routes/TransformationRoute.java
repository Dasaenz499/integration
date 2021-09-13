package org.company.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.company.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class TransformationRoute extends RouteBuilder {



    @Override
    public void configure() throws Exception {

        JacksonDataFormat formatUser = new JacksonDataFormat(User.class);

       

        from("direct:saveUser")
                .routeId("route-save-user")
                .to("micrometer:timer:create.user.timer?action=start")
                .log("Save User !!!")
                .log("${body}")
                .log("${body.age}")
                .to("sql:insert into user (firstName,lastName,phoneNumber, age) values (:#${body.firstName}, :#${body.lastName}, :#${body.phoneNumber}, :#${body.age});")
                .to("micrometer:summary:edad.create.user.histogram?value=${body.age}")
                .to("micrometer:counter:customCounterCreateUser?increment=1&tags=edad=${body.age}")
                .to("micrometer:timer:create.user.timer?action=stop")
                .end();

        from("direct:getUsers")
                .log("Get Users !!!")
                .to("micrometer:counter:customCounterQuarkus")
                .to("sql:select * from user u ;")
                .end();
    }
}
