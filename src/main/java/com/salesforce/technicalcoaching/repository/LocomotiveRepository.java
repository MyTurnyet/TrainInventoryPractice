package com.salesforce.technicalcoaching.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salesforce.technicalcoaching.enums.MaintenanceStatus;
import com.salesforce.technicalcoaching.enums.Scale;
import com.salesforce.technicalcoaching.model.Locomotive;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LocomotiveRepository {

    private String path = "data/locomotives.json";

    public List<Locomotive> findAll() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File f = new File(path);
            if (!f.exists()) {
                return new ArrayList<>();
            }
            List<Locomotive> list = mapper.readValue(f, new TypeReference<List<Locomotive>>() {});
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Locomotive findById(Long id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File f = new File(path);
            if (!f.exists()) {
                return null;
            }
            List<Locomotive> list = mapper.readValue(f, new TypeReference<List<Locomotive>>() {});
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

    public Locomotive save(Locomotive l) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File f = new File(path);
            List<Locomotive> list = new ArrayList<>();

            if (f.exists()) {
                list = mapper.readValue(f, new TypeReference<List<Locomotive>>() {});
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
            l.id = max + 1;
            l.createdDate = LocalDateTime.now();
            l.lastModifiedDate = LocalDateTime.now();

            list.add(l);
            mapper.writeValue(f, list);
            return l;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Locomotive update(Long id, Locomotive l) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File f = new File(path);
            List<Locomotive> list = mapper.readValue(f, new TypeReference<List<Locomotive>>() {});

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equals(id)) {
                    l.id = id;
                    l.createdDate = list.get(i).createdDate;
                    l.lastModifiedDate = LocalDateTime.now();
                    list.set(i, l);
                    mapper.writeValue(f, list);
                    return l;
                }
            }
            return null;
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
            List<Locomotive> list = mapper.readValue(f, new TypeReference<List<Locomotive>>() {});

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

    public List<Locomotive> findByScale(Scale s) {
        List<Locomotive> all = findAll();
        List<Locomotive> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).scale == s) {
                result.add(all.get(i));
            }
        }
        return result;
    }

    public List<Locomotive> findByStatus(MaintenanceStatus status) {
        List<Locomotive> all = findAll();
        List<Locomotive> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).maintenanceStatus == status) {
                result.add(all.get(i));
            }
        }
        return result;
    }
}
