package com.chf.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonUtil {

    private final static Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper mapper;

    private JsonUtil() {
    }

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            return (String) obj;
        }

        try (OutputStream os = new ByteArrayOutputStream()) {
            JsonGenerator generator = mapper.getFactory().createGenerator(os, JsonEncoding.UTF8);
            generator.writeObject(obj);
            return os.toString();
        } catch (IOException e) {
            LOG.warn("Cannot convert json from object {}", obj);
        }
        return obj.toString();
    }

    public static Object readValue(String json) {
        return readValue(json, Object.class);
    }

    public static <T> T readValue(String json, Class<T> cls) {
        if (json == null || json.length() == 0) {
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(json, cls);
        } catch (Exception e) {
            LOG.warn("Cannot convert json string to object {}", json);
        }
        return t;
    }

    public static <T> T readValue(byte[] bytes, Class<T> cls) {
        if (bytes == null) {
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(bytes, cls);
        } catch (Exception e) {
            LOG.warn("Cannot convert json inputStream to object {}");
        }
        return t;
    }

    public static <T> T readValue(InputStream is, Class<T> cls) {
        if (is == null) {
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(is, cls);
        } catch (Exception e) {
            LOG.warn("Cannot convert json inputStream to object {}");
        }
        return t;
    }

    @SuppressWarnings({ "unchecked" })
    public static Map<String, Object> readMapValue(String json) {
        return readValue(json, Map.class);
    }

    @SuppressWarnings({ "unchecked" })
    public static Map<String, Object> readMapValue(InputStream is) {
        return readValue(is, Map.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> fromJsonArrayString(String jsonArray, Class<T> cls) {
        List<Object> list = JsonUtil.readValue(jsonArray, List.class);
        List<T> resultList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Object item : list) {
                resultList.add(convertValue(item, cls));
            }
        }
        return resultList;
    }

    public static <T> T convertValue(Object obj, Class<T> cls) {
        if (obj == null) {
            return null;
        }
        return mapper.convertValue(obj, cls);
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

}
