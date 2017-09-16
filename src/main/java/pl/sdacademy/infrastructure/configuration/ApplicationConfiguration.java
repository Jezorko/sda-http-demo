package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import pl.sdacademy.infrastructure.properties.ClientProperties;

import javax.validation.Validator;
import java.util.Locale;

@Slf4j
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
    MessageSource taskDescriptions() {
        return messagesFrom("internationalization/tasks/descriptions");
    }

    @Bean
    MessageSource statusesDescriptions() {
        return messagesFrom("internationalization/statuses/descriptions");
    }

    @Bean
    MessageSource validationMessages() {
        return messagesFrom("internationalization/validation/ValidationMessages");
    }

    private MessageSource messagesFrom(String path) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(path);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(true);
        return messageSource;
    }

    @Bean
    Validator validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(validationMessages());
        return validator;
    }
}
