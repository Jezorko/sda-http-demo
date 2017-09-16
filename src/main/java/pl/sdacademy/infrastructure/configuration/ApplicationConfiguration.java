package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import pl.sdacademy.infrastructure.properties.ClientProperties;

import javax.validation.Validator;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final ClientProperties clientProperties;

    @Bean
    LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale(clientProperties.getDefaultLocale()));
        return localeResolver;
    }

    @Bean
    ResourceBundleMessageSource taskDescriptions() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("tasks/descriptions");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        resourceBundleMessageSource.setFallbackToSystemLocale(true);
        return resourceBundleMessageSource;
    }

    @Bean
    ResourceBundleMessageSource errorMessages() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("errors/messages");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        resourceBundleMessageSource.setFallbackToSystemLocale(true);
        return resourceBundleMessageSource;
    }

    @Bean
    FixedLocaleResolver fixedLocaleResolver() {
        FixedLocaleResolver fixedLocaleResolver = new FixedLocaleResolver();
        fixedLocaleResolver.setDefaultLocale(new Locale("en"));
        return fixedLocaleResolver;
    }

    @Bean
    Validator validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(errorMessages());
        return validator;
    }
}
