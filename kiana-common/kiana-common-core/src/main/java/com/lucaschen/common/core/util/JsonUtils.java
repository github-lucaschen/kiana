package com.lucaschen.common.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lucaschen.common.core.exception.GlobalException;
import com.lucaschen.common.core.exception.UtilExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public final class JsonUtils {

    private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final JsonMapper jsonMapper = JsonMapper.builder()
            // disable -- fail on unknown properties
            // ({"id":null,"field":"value"} -> Object.builder.field(value).build())
            // ignore non-existent field
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // non null field
            // ({"id":null,"field":"value"} -> {"field":"value"})
            // result ignore fields with null values
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            // enable -- allow single quotes
            // ({'field':"value"})
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            // enable -- allow unquoted field names
            // ({field:"value"})
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            // disable -- write dates as timestamps
            // enable ---- java.util.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"))
            //              --> Number(1704038400000)
            // disable ---- java.util.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"))
            //              --> String("2023-12-31T16:00:00.000+00:00")
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            // Set date formatting template
            // java.util.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"))
            // --> String("2024-01-01 00:00:00")
            .defaultDateFormat(new SimpleDateFormat(LOCAL_DATE_TIME_FORMAT)).build();

    static {
        // add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        // to enable handling Java 8 date/time type
        // set custom serializer and deserializer
        // to replace datetime format template [yyyy-MM-ddTHH:mm:ss] to [yyyy-MM-dd HH:mm:ss]
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        final DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new
                LocalDateTimeDeserializer(dateTimeFormatter));

        // 序列换成 json 时,将所有的 long 变成 string。因为 js 中的数字类型不能包含所有的 java long 值
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);

        jsonMapper.registerModule(javaTimeModule);
    }


    private JsonUtils() {
    }

    public static <T> String to(final T object) {
        if (Objects.isNull(object)) {
            return null;
        }
        try {
            return object instanceof String ? (String) object : jsonMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(MessageFormat.format("JsonUtils::to({0})", object), e);
            throw new GlobalException(UtilExceptionCode.UTIL_JSON_001);
        }
    }

    public static <T> T to(final String json, final Class<T> clazz) {
        if (StringUtils.isBlank(json) || Objects.isNull(clazz)) {
            return null;
        }
        try {
            return jsonMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error(MessageFormat.format("JsonUtils::to({0}, {1})",
                    json, clazz), e);
            throw new GlobalException(UtilExceptionCode.UTIL_JSON_001);
        }
    }

    public static <T> T to(final String json, final TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json) || Objects.isNull(typeReference)) {
            return null;
        }
        try {
            return jsonMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error(MessageFormat.format("JsonUtils::to({0}, {1})",
                    json, typeReference), e);
            throw new GlobalException(UtilExceptionCode.UTIL_JSON_001);
        }
    }

    public static boolean isJson(final String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            jsonMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            log.error(MessageFormat.format("JsonUtils::isJson({0})", json));
            return false;
        }
    }

    public static <T> String toPrettyPrinter(final T object) {
        if (Objects.isNull(object)) {
            return null;
        }
        try {
            if (object instanceof String) {
                return jsonMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(jsonMapper.readTree(object.toString()));
            } else {
                return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            }
        } catch (JsonProcessingException e) {
            log.error(MessageFormat.format("JsonUtils::to({0})", object), e);
            throw new GlobalException(UtilExceptionCode.UTIL_JSON_001);
        }
    }

    public static List<String> findText(final String json, final String fieldName) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(fieldName)) {
            return Collections.emptyList();
        }
        try {
            return jsonMapper.readTree(json).findValuesAsText(fieldName);
        } catch (JsonProcessingException e) {
            log.error(MessageFormat.format("JsonUtils::findText({0}, {1})",
                    json, fieldName), e);
            throw new GlobalException(UtilExceptionCode.UTIL_JSON_001);
        }
    }

    public static String find(final String json, final String fieldName) {
        return findText(json, fieldName).stream().findFirst().orElse(null);
    }

    public static JsonNode toTree(final String json) {
        if (StringUtils.isNoneBlank(json)) {
            try {
                return jsonMapper.readTree(json);
            } catch (JsonProcessingException e) {
                log.error(MessageFormat.format("JsonUtils::toTree({0})",
                        json), e);
                throw new GlobalException(UtilExceptionCode.UTIL_JSON_001);
            }
        }
        return null;
    }

    public static <T> T convert(final Object object, final Class<T> clazz) {
        if (Objects.isNull(object) || Objects.isNull(clazz)) {
            return null;
        }
        return jsonMapper.convertValue(object, clazz);
    }

    public static <T> T convert(final Object object, final TypeReference<T> typeReference) {
        if (Objects.isNull(object) || Objects.isNull(typeReference)) {
            return null;
        }
        return jsonMapper.convertValue(object, typeReference);
    }
}