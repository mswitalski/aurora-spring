package pl.lodz.p.aurora.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.aurora.common.domain.dto.ValidationMessageDto;
import pl.lodz.p.aurora.common.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.common.util.Translator;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Handler for failed controller validation of passed objects.
 */
@ControllerAdvice
public class ControllerValidationHandler {

    private final Translator translator;

    @Autowired
    public ControllerValidationHandler(Translator translator) {
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
    public List<ValidationMessageDto> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        return bindingResult.getFieldErrors().stream().map(this::constructValidationMessage).collect(Collectors.toList());
    }

    private ValidationMessageDto constructValidationMessage(FieldError error) {
        return new ValidationMessageDto(error.getDefaultMessage(), error.getField());
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processValidationError2(@RequestHeader("Accept-Language") Locale locale,
                                                              UniqueConstraintViolationException exception) {
        return exception.getFieldsNames().stream()
                .map(f -> constructValidationMessage(
                        translator.translate(exception.getEntityName() + "." + f + ".Unique", locale),
                        f
                )).collect(Collectors.toList());
    }

    private ValidationMessageDto constructValidationMessage(String message, String fieldName) {
        return new ValidationMessageDto(message, fieldName);
    }
}
