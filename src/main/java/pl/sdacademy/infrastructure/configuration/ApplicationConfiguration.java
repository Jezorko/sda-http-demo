package pl.sdacademy.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.validation.Validator;
import java.util.Locale;

@Configuration
public class ApplicationConfiguration {
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
    public Validator validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(errorMessages());
        return validator;
    }
}
