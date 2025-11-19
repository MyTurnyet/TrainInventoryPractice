package com.salesforce.technicalcoaching.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salesforce.technicalcoaching.enums.AARType;
import com.salesforce.technicalcoaching.model.RollingStock;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RollingStockRepository {

    private String path = "data/rolling-stock.json";

    public List<RollingStock> findAll() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File f = new File(path);
            if (!f.exists()) {
                return new ArrayList<>();
            }
            List<RollingStock> list = mapper.readValue(f, new TypeReference<List<RollingStock>>() {});
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public RollingStock findById(Long id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File f = new File(path);
            if (!f.exists()) {
                return null;
            }
            List<RollingStock> list = mapper.readValue(f, new TypeReference<List<RollingStock>>() {});
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

    public RollingStock save(RollingStock rs) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File f = new File(path);
            List<RollingStock> list = new ArrayList<>();

            if (f.exists()) {
                list = mapper.readValue(f, new TypeReference<List<RollingStock>>() {});
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
            rs.id = max + 1;
            rs.createdDate = LocalDateTime.now();
            rs.lastModifiedDate = LocalDateTime.now();

            list.add(rs);
            mapper.writeValue(f, list);
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RollingStock update(Long id, RollingStock rs) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File f = new File(path);
            List<RollingStock> list = mapper.readValue(f, new TypeReference<List<RollingStock>>() {});

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equals(id)) {
                    rs.id = id;
                    rs.createdDate = list.get(i).createdDate;
                    rs.lastModifiedDate = LocalDateTime.now();
                    list.set(i, rs);
                    mapper.writeValue(f, list);
                    return rs;
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
            List<RollingStock> list = mapper.readValue(f, new TypeReference<List<RollingStock>>() {});

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

    public List<RollingStock> findByAarType(AARType type) {
        List<RollingStock> all = findAll();
        List<RollingStock> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).aarType == type) {
                result.add(all.get(i));
            }
        }
        return result;
    }
}
