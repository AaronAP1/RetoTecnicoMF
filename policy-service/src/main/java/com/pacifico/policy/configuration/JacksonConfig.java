package com.pacifico.policy.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.http.codec.ServerCodecConfigurer;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig implements WebFluxConfigurer {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Registrar el módulo de Java Time
        mapper.registerModule(new JavaTimeModule());

        // Crear módulo personalizado para formato de fecha
        SimpleModule customModule = new SimpleModule();
        customModule.addSerializer(Instant.class, new CustomInstantSerializer());
        mapper.registerModule(customModule);

        // Deshabilitar timestamps para fechas
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    // Serializador personalizado para Instant
    private static class CustomInstantSerializer extends JsonSerializer<Instant> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")
                .withZone(java.time.ZoneOffset.UTC);

        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(FORMATTER.format(value));
        }
    }

    @Bean
    public Jackson2JsonEncoder jackson2JsonEncoder(ObjectMapper objectMapper) {
        return new Jackson2JsonEncoder(objectMapper);
    }

    @Bean
    public Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper objectMapper) {
        return new Jackson2JsonDecoder(objectMapper);
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        // Configurar los codecs HTTP con nuestro ObjectMapper personalizado
        configurer.defaultCodecs().jackson2JsonEncoder(jackson2JsonEncoder(objectMapper()));
        configurer.defaultCodecs().jackson2JsonDecoder(jackson2JsonDecoder(objectMapper()));
    }
}
