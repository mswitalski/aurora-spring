package pl.lodz.p.aurora.configuration.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Listener for any global exceptions that occurred during the work of the application.
 */
@ControllerAdvice
public class GlobalExceptionListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void processInvalidClientRequest(ServletRequestBindingException exception) {
        logger.error("Client sent an invalid request", exception);
    }
}
