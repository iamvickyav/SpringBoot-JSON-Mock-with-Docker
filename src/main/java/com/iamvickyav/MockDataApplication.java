package com.iamvickyav;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.io.File;

import static com.iamvickyav.MockDataConstants.FOLDER_PATH;

@SpringBootApplication
public class MockDataApplication {

	Logger LOGGER = LoggerFactory.getLogger(MockDataApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MockDataApplication.class, args);
	}

	@Bean
	ObjectMapper objectMapper(){
		ObjectMapper mapper = new ObjectMapper();
		return mapper;
	}

	@PostConstruct
	void validateJsonFolderAvailability() {
		File fi = new File(FOLDER_PATH);
		if(fi.isDirectory()){
			LOGGER.info("mockFiles folder is available");
		} else {
			LOGGER.warn("mockFiles folder is not available, creating new one");
			boolean status = fi.mkdir();
			LOGGER.info("mockFiles folder creation success={}", status);
		}
	}
}
