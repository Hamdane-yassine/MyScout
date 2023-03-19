package com.hamdane.myscoutdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MyScoutDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyScoutDiscoveryApplication.class, args);
    }

}
