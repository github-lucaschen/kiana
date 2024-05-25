package com.lucaschen.common.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lucaschen.common.web.advice.ExceptionControllerAdvice;
import com.lucaschen.common.web.advice.ResponseControllerAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GlobalWebConfig {

    private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public ObjectMapper objectMapper() {

        final ObjectMapper objectMapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .defaultDateFormat(new SimpleDateFormat(LOCAL_DATE_TIME_FORMAT))
                .build();

        final JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Java 8  时间序列化
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(LOCAL_DATE_TIME_FORMAT);

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ISO_DATE));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ISO_DATE));

        // 序列换成 JSON 时,将所有的 Long 变成 String。因为 JS 中的数字类型不能包含所有的 Java Long 值
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    @Bean
    public ExceptionControllerAdvice exceptionControllerAdvice() {
        return new ExceptionControllerAdvice();
    }

    @Bean
    public ResponseControllerAdvice responseControllerAdvice() {
        return new ResponseControllerAdvice();
    }
}
