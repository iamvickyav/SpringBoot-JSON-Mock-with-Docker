package com.iamvickyav;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.iamvickyav.MockDataConstants.*;

@RestController
public class MockDataController {

    Logger LOGGER = LoggerFactory.getLogger(MockDataController.class);

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping(value = "/mockService/{serviceName}")
    ResponseEntity createMockService(@PathVariable("serviceName") String serviceName,
                                     @RequestBody JsonNode body) throws IOException {

        LOGGER.info("createMockService called for serviceName={}", serviceName);

        File mockServiceJsonFile = getFile(serviceName);
        objectMapper.writeValue(mockServiceJsonFile, body);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put(MESSAGE, "Mock data created for " + serviceName);
        return new ResponseEntity(responseBody, HttpStatus.CREATED);
    }

    @GetMapping(value = "/mockService/{serviceName}")
    ResponseEntity fetchMockValue(@PathVariable("serviceName") String serviceName) throws IOException {

        LOGGER.info("fetchMockValue called for serviceName={}", serviceName);

        ResponseEntity responseEntity;
        File mockServiceJsonFile = getFile(serviceName);

        if(mockServiceJsonFile.exists()) {
            LOGGER.info("Mock Data Exist for serviceName={}", serviceName);
            JsonNode jsonNode = objectMapper.readValue(mockServiceJsonFile, JsonNode.class);
            responseEntity =  new ResponseEntity(jsonNode, HttpStatus.OK);
        }
        else {
            LOGGER.info("Mock Data not available for serviceName={}", serviceName);
            Map responseMap = new HashMap();
            responseMap.put(MESSAGE, "Mock data not available for " + serviceName);
            responseEntity = new ResponseEntity(responseMap, HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping("/mockService")
    List fetchAllMockServices() throws IOException {

        LOGGER.info("fetchAllMockServices called");

        Stream<Path> walk = Files.walk(Paths.get(FOLDER_PATH));

        List<String> existingServices = walk.map(Path::toString)
                .filter(file -> file.endsWith(JSON_EXTENSION))
                .map(str -> str.replace(FOLDER_PATH, ""))
                .map(str -> str.replace(JSON_EXTENSION, ""))
                .collect(Collectors.toList());

        return existingServices;
    }

    private File getFile(String serviceName) {
         return new File(FOLDER_PATH + serviceName + JSON_EXTENSION);
    }
}
