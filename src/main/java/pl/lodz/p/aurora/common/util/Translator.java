package pl.lodz.p.aurora.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Utility class that simplifies internationalization of messages.
 */
@Component
public class Translator {

    private final MessageSource messageSource;

    @Autowired
    public Translator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Translate requested message by its unique key defined in resource bundle.
     *
     * @param messageKey Message's key from resource bundle
     * @param activeLocale Active locale set for request
     * @return Translated message
     */
    public String translate(String messageKey, Locale activeLocale) {
        return messageSource.getMessage(messageKey, null, activeLocale);
    }
}
