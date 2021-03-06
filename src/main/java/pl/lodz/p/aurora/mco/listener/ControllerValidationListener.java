package pl.lodz.p.aurora.mco.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lodz.p.aurora.msh.web.dto.ValidationMessageDto;
import pl.lodz.p.aurora.msh.exception.InvalidEntityStateException;
import pl.lodz.p.aurora.msh.exception.OutdatedEntityModificationException;
import pl.lodz.p.aurora.msh.exception.UniqueConstraintViolationException;
import pl.lodz.p.aurora.msh.util.Translator;
import pl.lodz.p.aurora.mme.exception.IncompetentMentorException;
import pl.lodz.p.aurora.mme.exception.SelfFeedbackException;
import pl.lodz.p.aurora.mme.exception.TooFrequentFeedbackException;
import pl.lodz.p.aurora.mta.exception.InvalidDateException;
import pl.lodz.p.aurora.mtr.exception.InvalidDateTimeException;
import pl.lodz.p.aurora.mtr.exception.OverpeopledTrainingException;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Listener for failed controller validation of passed objects.
 */
@ControllerAdvice
public class ControllerValidationListener {

    private final Translator translator;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        logger.info(exception.getMessage());
        BindingResult bindingResult = exception.getBindingResult();

        return bindingResult.getFieldErrors().stream().map(this::constructValidationMessage).collect(Collectors.toList());
    }

    private ValidationMessageDto constructValidationMessage(FieldError error) {
        return new ValidationMessageDto(error.getDefaultMessage(), error.getField());
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processUniqueValidationError(UniqueConstraintViolationException exception) {
        logger.info(exception.getMessage());
        Locale locale = LocaleContextHolder.getLocale();
        String translatedMessage = translator
                .translate(exception.getEntityName() + "." + exception.getFieldName() + ".Unique", locale);

        return Collections.singletonList(new ValidationMessageDto(translatedMessage, exception.getFieldName()));
    }

    @ExceptionHandler(InvalidEntityStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void processInvalidEntityState(InvalidEntityStateException exception) {
        logger.error(exception.getMessage(), exception);
    }

    @ExceptionHandler(OutdatedEntityModificationException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public void processOutdatedEntityModification(OutdatedEntityModificationException exception) {
        logger.info(exception.getMessage());
    }

    @ExceptionHandler(IncompetentMentorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processIncompetentMentor(IncompetentMentorException exception) {
        logger.info(exception.getMessage());
        Locale locale = LocaleContextHolder.getLocale();
        String translatedMessage = translator.translate("Mentor.skill.insufficientLevel", locale);

        return Collections.singletonList(new ValidationMessageDto(translatedMessage, "skill"));
    }

    @ExceptionHandler(SelfFeedbackException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processSelfFeedback(SelfFeedbackException exception) {
        logger.info(exception.getMessage());
        Locale locale = LocaleContextHolder.getLocale();
        String translatedMessage = translator.translate("Feedback.user.selfFeedback", locale);

        return Collections.singletonList(new ValidationMessageDto(translatedMessage, "user"));
    }

    @ExceptionHandler(TooFrequentFeedbackException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processTooFrequentFeedback(TooFrequentFeedbackException exception) {
        logger.info(exception.getMessage());
        Locale locale = LocaleContextHolder.getLocale();
        String translatedMessage = translator.translate("Feedback.createDateTime.onePerDay", locale);

        return Collections.singletonList(new ValidationMessageDto(translatedMessage, "user"));
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processDateTimeParse(DateTimeParseException exception) {
        logger.info(exception.getMessage(), exception);
        Locale locale = LocaleContextHolder.getLocale();
        String translatedMessage = translator.translate("DateTime.invalidFormat", locale);

        return Collections.singletonList(new ValidationMessageDto(translatedMessage, "date"));
    }

    @ExceptionHandler(InvalidDateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processInvalidDateTime(InvalidDateTimeException exception) {
        List<ValidationMessageDto> messages = new ArrayList<>();
        Locale locale = LocaleContextHolder.getLocale();

        if (exception.getErrors().contains(InvalidDateTimeException.ERROR.START_BEFORE_NOW)) {
            String translatedMessage = translator.translate("Training.startDateTime.beforeNow", locale);
            messages.add(new ValidationMessageDto(translatedMessage, "startDateTime"));
        }
        if (exception.getErrors().contains(InvalidDateTimeException.ERROR.END_BEFORE_NOW)) {
            String translatedMessage = translator.translate("Training.endDateTime.beforeNow", locale);
            messages.add(new ValidationMessageDto(translatedMessage, "endDateTime"));
        }
        if (exception.getErrors().contains(InvalidDateTimeException.ERROR.END_BEFORE_EQUAL_START)) {
            String translatedMessage = translator.translate("Training.endDateTime.beforeEqualStart", locale);
            messages.add(new ValidationMessageDto(translatedMessage, "endDateTime"));
        }

        return messages;
    }

    @ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processInvalidDate(InvalidDateException exception) {
        List<ValidationMessageDto> messages = new ArrayList<>();
        Locale locale = LocaleContextHolder.getLocale();

        if (exception.getErrors().contains(InvalidDateException.ERROR.DATE_BEFORE_TODAY)) {
            String translatedMessage = translator.translate("Task.deadlineDate.dateBeforeToday", locale);
            messages.add(new ValidationMessageDto(translatedMessage, "deadlineDate"));
        }

        return messages;
    }

    @ExceptionHandler(OverpeopledTrainingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ValidationMessageDto> processOverpeopledTraining(OverpeopledTrainingException exception) {
        logger.info(exception.getMessage());
        Locale locale = LocaleContextHolder.getLocale();
        String translatedMessage = translator.translate("Training.participantsLimit.overpeopled", locale);

        return Collections.singletonList(new ValidationMessageDto(translatedMessage, "participantsLimit"));
    }
}
