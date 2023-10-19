package net.javaguides.organizationservice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@OpenAPIDefinition(
		info=@Info(
				title = "Organization Service REST APIs",
				description= "Organization Service REST APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Nirbhay",
						email = "javaguides.net@gmail.com",
						url = "javaguides.net"
				),
				license =  @License(
						name="Apache 2.0",
						url="javaguides.net/license"
				)
		),
		externalDocs=@ExternalDocumentation(
				description = "Organization Service Docs",
				url = "javaguides.net/Organization_Service.html"
		)
)

//@EnableEurekaClient
public class OrganizationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganizationServiceApplication.class, args);
	}

}
