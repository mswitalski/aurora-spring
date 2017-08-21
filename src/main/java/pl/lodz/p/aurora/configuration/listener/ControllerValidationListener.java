package pl.lodz.p.aurora.configuration.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.domain.dto.ValidationMessageDto;
import pl.lodz.p.aurora.common.exception.InvalidEntityStateException;
import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.common.util.Translator;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Listener for failed controller validation of passed objects.
 */
@ControllerAdvice
public class ControllerValidationListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Translator translator;

    @Autowired
    public ControllerValidationListener(Translator translator) {
        this.translator = translator;
    }

    /**
     * Process validation errors.
     *
     * @param exception Exception related to invalid method argument
     * @return List of validation error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processBeanValidationError(MethodArgumentNotValidException exception) {
        logger.info("Data provided by user did not pass validation phase", exception);

        BindingResult bindingResult = exception.getBindingResult();

        return bindingResult.getFieldErrors().stream().map(this::constructValidationMessage).collect(Collectors.toList());
    }

    private ValidationMessageDto constructValidationMessage(FieldError error) {
        return new ValidationMessageDto(error.getDefaultMessage(), error.getField());
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processUniqueValidationError(@RequestHeader("Accept-Language") Locale locale,
                                                                   UniqueConstraintViolationException exception) {
        logger.info("Data provided by user was not unique", exception);

        return exception.getFieldsNames().stream()
                .map(f -> constructValidationMessage(
                        translator.translate(exception.getEntityName() + "." + f + ".Unique", locale),
                        f
                )).collect(Collectors.toList());
    }

    private ValidationMessageDto constructValidationMessage(String message, String fieldName) {
        return new ValidationMessageDto(message, fieldName);
    }

    @ExceptionHandler(InvalidEntityStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void processInvalidEntityState(InvalidEntityStateException exception) {
        logger.error("Application tried to save entity with invalid state", exception);
    }
}