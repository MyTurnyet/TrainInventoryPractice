package com.salesforce.technicalcoaching.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salesforce.technicalcoaching.model.MaintenanceLog;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MaintenanceLogRepository {

    private String path = "data/maintenance-logs.json";

    public List<MaintenanceLog> findAll() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File f = new File(path);
            if (!f.exists()) {
                return new ArrayList<>();
            }
            List<MaintenanceLog> list = mapper.readValue(f, new TypeReference<List<MaintenanceLog>>() {});
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public MaintenanceLog findById(Long id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File f = new File(path);
            if (!f.exists()) {
                return null;
            }
            List<MaintenanceLog> list = mapper.readValue(f, new TypeReference<List<MaintenanceLog>>() {});
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equals(id)) {
                    return list.get(i);
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MaintenanceLog save(MaintenanceLog log) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File f = new File(path);
            List<MaintenanceLog> list = new ArrayList<>();

            if (f.exists()) {
                list = mapper.readValue(f, new TypeReference<List<MaintenanceLog>>() {});
            } else {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            long max = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id > max) {
                    max = list.get(i).id;
                }
            }
            log.id = max + 1;
            log.createdDate = LocalDateTime.now();

            list.add(log);
            mapper.writeValue(f, list);
            return log;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteById(Long id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File f = new File(path);
            List<MaintenanceLog> list = mapper.readValue(f, new TypeReference<List<MaintenanceLog>>() {});

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equals(id)) {
                    list.remove(i);
                    break;
                }
            }
            mapper.writeValue(f, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MaintenanceLog> findByItem(Long itemId) {
        List<MaintenanceLog> all = findAll();
        List<MaintenanceLog> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).inventoryItemId.equals(itemId)) {
                result.add(all.get(i));
            }
        }
        return result;
    }
}
