package com.hamdane.graphsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableNeo4jRepositories
public class GraphSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphSvcApplication.class, args);
	}

}
