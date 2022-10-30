package pl.edu.pjwstk.s22796.xyz.cinemareservation;

import jakarta.persistence.NoResultException;
import jakarta.persistence.RollbackException;
import jakarta.servlet.ServletException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;
import pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata.ErrorInformation;

import java.time.DateTimeException;

@Provider
public class CinemaExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
            if(throwable instanceof ServletException)
                throwable = throwable.getCause();
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            if (throwable instanceof RollbackException)
                if(throwable.getCause() != null)
                    if(throwable.getCause().getCause() instanceof ConstraintViolationException) {
                        throwable = new SeatReservedException(ReservationResource.lastSeatResAttempt);
                    }
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
    }

}
