package com.softwareascraft.practice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softwareascraft.practice.exception.JsonFileException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Static utility class for JSON file operations (ANTI-PATTERN for teaching purposes)
 * - Cannot be mocked in tests
 * - Hard-coded file paths
 * - No abstraction or dependency injection
 */
public class JsonFileManager {

    // Hard-coded base directory path (ANTI-PATTERN)
    private static final String DATA_DIRECTORY = "data/";

    private static final ObjectMapper objectMapper = createObjectMapper();

    // Private constructor to prevent instantiation
    private JsonFileManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    /**
     * Reads data from a JSON file
     * @param fileName the name of the file (without path)
     * @param typeReference the type reference for deserialization
     * @return list of objects from the file
     */
    public static <T> List<T> readFromFile(String fileName, TypeReference<List<T>> typeReference) {
        ensureFileExists(fileName);
        File file = new File(DATA_DIRECTORY + fileName);

        try {
            List<T> data = objectMapper.readValue(file, typeReference);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            throw new JsonFileException("Failed to read from file: " + fileName, e);
        }
    }

    /**
     * Writes data to a JSON file
     * @param fileName the name of the file (without path)
     * @param data the data to write
     */
    public static <T> void writeToFile(String fileName, List<T> data) {
        ensureDirectoryExists();
        File file = new File(DATA_DIRECTORY + fileName);

        try {
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            throw new JsonFileException("Failed to write to file: " + fileName, e);
        }
    }

    /**
     * Ensures the data directory exists
     */
    private static void ensureDirectoryExists() {
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Ensures a specific file exists, creates it with empty array if not
     */
    public static void ensureFileExists(String fileName) {
        ensureDirectoryExists();
        File file = new File(DATA_DIRECTORY + fileName);

        if (!file.exists()) {
            try {
                objectMapper.writeValue(file, new ArrayList<>());
            } catch (IOException e) {
                throw new JsonFileException("Failed to create file: " + fileName, e);
            }
        }
    }
}
