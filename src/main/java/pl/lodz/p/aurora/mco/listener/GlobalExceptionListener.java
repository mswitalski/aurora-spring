package pl.lodz.p.aurora.mco.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import pl.lodz.p.aurora.min.outlook.exception.OutlookApiException;
import pl.lodz.p.aurora.msh.exception.ActionForbiddenException;
import pl.lodz.p.aurora.msh.exception.InvalidApplicationConfigurationException;
import pl.lodz.p.aurora.msh.exception.InvalidRequestException;
import pl.lodz.p.aurora.msh.exception.InvalidResourceRequestedException;

/**
 * Listener for any global exceptions that occurred during the work of the application.
 */
@ControllerAdvice
public class GlobalExceptionListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void processInvalidClientRequest(ServletRequestBindingException exception) {
        logger.info("Client sent an invalid request", exception);
    }

    @ExceptionHandler(InvalidResourceRequestedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void processInvalidResourceRequest(InvalidResourceRequestedException exception) {
        logger.info(exception.getMessage());
    }

    @ExceptionHandler(InvalidApplicationConfigurationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void processInvalidApplicationConfiguration(InvalidApplicationConfigurationException exception) {
        logger.error(exception.getMessage(), exception);
    }

    @ExceptionHandler(ActionForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void processActionForbidden(ActionForbiddenException exception) {
        logger.warn(exception.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void processInvalidRequest(InvalidRequestException exception) {
        logger.warn(exception.getMessage(), exception);
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void processRestClient(RestClientException exception) {
        logger.error(exception.getMessage(), exception);
    }

    @ExceptionHandler(OutlookApiException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public void processOutlookApi(OutlookApiException exception) {
        logger.error(exception.getMessage(), exception);
    }
}
