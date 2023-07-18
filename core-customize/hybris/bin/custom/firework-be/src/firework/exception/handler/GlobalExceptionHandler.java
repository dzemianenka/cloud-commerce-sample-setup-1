package firework.exception.handler;

import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.resolve;

@RestControllerAdvice("firework.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(FlexibleSearchException.class)
    ResponseEntity<FireworkExceptionDto> handleFlexibleSearchException(FlexibleSearchException e) {
        return new ResponseEntity<>(new FireworkExceptionDto(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    ResponseEntity<FireworkExceptionDto> handleModelNotFoundException(ModelNotFoundException e) {
        return new ResponseEntity<>(new FireworkExceptionDto(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(RestClientResponseException.class)
    ResponseEntity<FireworkExceptionDto> handleRestException(RestClientResponseException e) {
        return new ResponseEntity<>(new FireworkExceptionDto(e.getMessage()),
                requireNonNull(resolve(e.getRawStatusCode())));
    }

    @ExceptionHandler(UnknownIdentifierException.class)
    ResponseEntity<FireworkExceptionDto> handleUnknownIdentifierException(UnknownIdentifierException e) {
        return new ResponseEntity<>(new FireworkExceptionDto(e.getMessage()), NOT_FOUND);
    }
}
