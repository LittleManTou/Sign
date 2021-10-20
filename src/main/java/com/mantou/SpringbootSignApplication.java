package com.mantou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

@SpringBootApplication
public class SpringbootSignApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSignApplication.class, args);
    }


}
