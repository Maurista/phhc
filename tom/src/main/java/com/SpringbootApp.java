package com;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 */
@SpringBootApplication
public class SpringbootApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApp.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
    }
}