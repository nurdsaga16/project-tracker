package com.sdu_ai_lab.project_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProjectTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTrackerApplication.class, args);
	}

}
