package pl.lodz.p.aurora.msh.validator;

import org.junit.Test;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

public class NoHtmlValidatorTest {
    private ConstraintValidator<NoHtml, String> sut = new NoHtmlValidator();
    private ConstraintValidatorContext fakeContext = null;

    @Test
    public void shouldReturnFalseWhenProvidedHtmlCode() {
        // Arrange
        String htmlCode = "<html><head><title>Hello!</title></head><body>World</body></html>";

        // Act
        boolean result = sut.isValid(htmlCode, fakeContext);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenProvidedPlainText() {
        // Arrange
        String plainText = "My name is Jack";

        // Act
        boolean result = sut.isValid(plainText, fakeContext);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnTrueWhenEncodedText() {
        // Arrange
        String plainText = "&#x3C;html&#x3E;&#x3C;head&#x3E;&#x3C;title&#x3E;Hello!&#x3C;/title&#x3E;"
                + "&#x3C;/head&#x3E;&#x3C;body&#x3E;World&#x3C;/body&#x3E;&#x3C;/html&#x3E;";

        // Act
        boolean result = sut.isValid(plainText, fakeContext);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenProvidedPlainTextWithAccidentalHtml() {
        // Arrange
        String plainText = "The value of x is < 5 but > 0";

        // Act
        boolean result = sut.isValid(plainText, fakeContext);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenProvidedPlainTextWithInqeualitySigns() {
        // Arrange
        String plainText = "Some people are > just happy <!";

        // Act
        boolean result = sut.isValid(plainText, fakeContext);

        // Assert
        assertThat(result).isTrue();
    }
}