package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.sdacademy.infrastructure.configuration.handlers.ObservableReturnValueHandler;

import java.util.List;

@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class WebConfiguration extends WebMvcConfigurerAdapter {

    private final ApplicationConfiguration applicationConfiguration;
    private final HttpMessageConverters httpMessageConverters;

    @Override
    public Validator getValidator() {
        return (Validator) applicationConfiguration.validator();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(httpMessageConverters.getConverters());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new ObservableReturnValueHandler());
    }

}
