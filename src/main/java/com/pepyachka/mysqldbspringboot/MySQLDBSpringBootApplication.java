package com.pepyachka.mysqldbspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class MySQLDBSpringBootApplication {

    public static void main(String[] args) {

//        ApiContextInitializer.init();
        SpringApplication.run(MySQLDBSpringBootApplication.class, args);

    }


}
