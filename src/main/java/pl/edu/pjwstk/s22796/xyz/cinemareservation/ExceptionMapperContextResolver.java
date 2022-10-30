package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import jakarta.persistence.NoResultException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.ErrorInformation;

import java.time.DateTimeException;

@Provider
@SuppressWarnings("unused")
public class ExceptionMapperContextResolver implements ContextResolver<ExceptionMapper<Throwable>> {

    private final ExceptionMapper<Throwable> mapper;

    public ExceptionMapperContextResolver() {
        mapper = throwable -> {
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            if (throwable instanceof IllegalArgumentException ||
                    throwable instanceof DateTimeException ||
                    throwable instanceof SeatReservedException)
                status = Response.Status.BAD_REQUEST;
            if (throwable instanceof NoResultException)
                status = Response.Status.NOT_FOUND;
            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorInformation(throwable.getMessage()))
                    .build();
        };
    }

    @Override
    public ExceptionMapper<Throwable> getContext(Class<?> aClass) {
        return mapper;
    }

}
