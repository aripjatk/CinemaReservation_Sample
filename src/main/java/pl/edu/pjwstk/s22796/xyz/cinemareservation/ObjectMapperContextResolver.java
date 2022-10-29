package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.SeatingAvailability;

import java.io.IOException;

/**
 * Handles conversion of objects to JSON. This implementation enables conversion
 * of SeatingAvailability and temporal objects such as LocalDateTime.
 */
@Provider
@SuppressWarnings("unused")
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        SimpleModule serializationModule = new SimpleModule();
        serializationModule.addSerializer(SeatingAvailability.class, new JsonSerializer<>() {
            @Override
            public void serialize(SeatingAvailability seatingAvailability, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(seatingAvailability.toString());
            }
        });
        mapper.registerModule(serializationModule);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}