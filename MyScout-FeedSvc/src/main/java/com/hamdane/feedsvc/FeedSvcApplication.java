package com.hamdane.feedsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableCassandraRepositories
public class FeedSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedSvcApplication.class, args);
	}

}
