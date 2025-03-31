package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"classpath:/color.properties"})
public class BooksApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class, args);
    }
}