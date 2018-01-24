package io.provenance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
 
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(new ApiInfoBuilder().title("Provenance API").version("1.0.0")
        		  .description("Provenance API allows getting Cluster topology, Cluster stats, Cluster provenance data as well as Provenance of a particular data value.")
        		  .contact(new Contact("Mukrram Ur Rahman", null, "mukrram.rahman@gmail.com"))
        		  .contact(new Contact("Vinothkumar Nagasayanan", null, "cegvinoth@gmail.com")).build());
    }
}