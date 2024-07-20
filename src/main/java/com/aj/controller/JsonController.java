package com.aj.controller;

import com.aj.entity.JsonEntity;
import com.aj.model.JsonModel;
import com.aj.repository.JsonModelRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "JSON Manipulation", description = "APIs for manipulating and retrieving JSON data")
public class JsonController {

    private static final Logger logger = LoggerFactory.getLogger(JsonController.class);

    @Autowired
    private JsonModelRepository jsonModelRepository;

    @PostMapping("/manipulate")
    @Operation(summary = "Manipulate JSON", description = "Manipulate JSON based on input and store the result")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Error processing JSON")
    public ResponseEntity<Map<String, Object>> manipulateJson(@RequestParam String input) {
        try {

            String modifiedJson = manipulateJsonModel(input);

            JsonEntity jsonEntity = new JsonEntity(modifiedJson);
            jsonModelRepository.save(jsonEntity);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> response = mapper.readValue(modifiedJson, new TypeReference<Map<String, Object>>() {});
            logger.info("JSON manipulation successful");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error processing JSON", e);
            return ResponseEntity.status(500).body(Map.of("error", "Error processing JSON"));
        }
    }

    @GetMapping("/list")
    @Operation(summary = "List JSON models", description = "Retrieve all stored JSON models")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Error retrieving JSON models")
    public ResponseEntity<List<JsonEntity>> listJsonModels() {
        try {

            List<JsonEntity> jsonEntities = jsonModelRepository.findAll();
            return ResponseEntity.ok(jsonEntities);
        } catch (Exception e) {
            logger.error("Error retrieving JSON models", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    private String manipulateJsonModel(String input) throws IOException {
        String json = JsonModel.JSON;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});

        String[] pairs = input.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":::");
            if (keyValue.length == 2) {
                replaceInJsonMap(jsonMap, keyValue[0], keyValue[1]);
            }
        }

        return mapper.writeValueAsString(jsonMap);
    }

    @SuppressWarnings("unchecked")
    private void replaceInJsonMap(Map<String, Object> jsonMap, String target, String replacement) {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            if (entry.getValue() instanceof String) {
                if (entry.getValue().equals(target)) {
                    entry.setValue(replacement);
                }
            } else if (entry.getValue() instanceof Map) {
                replaceInJsonMap((Map<String, Object>) entry.getValue(), target, replacement);
            } else if (entry.getValue() instanceof List) {
                for (Object item : (List<Object>) entry.getValue()) {
                    if (item instanceof Map) {
                        replaceInJsonMap((Map<String, Object>) item, target, replacement);
                    }
                }
            }
        }
    }
}
