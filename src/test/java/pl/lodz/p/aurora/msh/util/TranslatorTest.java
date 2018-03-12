package pl.lodz.p.aurora.msh.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TranslatorTest {
    @Mock
    private MessageSource mockedMessageSource;

    @InjectMocks
    private Translator sut;

    @Test
    public void shouldReturnTranslatedMessageWhenProvidedExistingKey() {
        // Arrange
        String validMessageKey = "someKey";
        String desiredTranslatedMessage = "Some Message";
        Locale fakeActiveLocale = Locale.ENGLISH;
        when(mockedMessageSource.getMessage(validMessageKey, null, fakeActiveLocale))
                .thenReturn(desiredTranslatedMessage);

        // Act
        String actualTranslatedMessage = sut.translate(validMessageKey, fakeActiveLocale);

        // Assert
        assertThat(actualTranslatedMessage).isEqualTo(desiredTranslatedMessage);
    }

    @Test
    public void shouldThrowExceptionWhenProvidedNonexistingKey() {
        // Arrange
        String invalidMessageKey = "someInvalidKey";
        Locale fakeActiveLocale = Locale.ENGLISH;
        when(mockedMessageSource.getMessage(invalidMessageKey, null, fakeActiveLocale))
                .thenThrow(NoSuchMessageException.class);

        // Assert
        assertThatExceptionOfType(NoSuchMessageException.class)
                .isThrownBy(() -> sut.translate(invalidMessageKey, fakeActiveLocale));
    }

}