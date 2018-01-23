package pl.lodz.p.aurora;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Main class for Aurora application.
 */
@ComponentScan
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
public class AuroraApplication extends SpringBootServletInitializer {

    @Value("${aurora.i18n.supported-languages}")
    private String supportedLanguages;

    @Value("${aurora.i18n.default-language}")
    private String defaultLanguage;

    public static void main(String[] args) {
        SpringApplication.run(AuroraApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag(defaultLanguage));
        resolver.setSupportedLocales(Arrays.stream(supportedLanguages.split(","))
                .map(Locale::forLanguageTag).collect(Collectors.toList()));

        return resolver;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AuroraApplication.class);
    }
}
