package pl.sdacademy.infrastructure.configuration;

import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public class DemoBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    @SneakyThrows
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
        response.setStatus(SC_UNAUTHORIZED);
        response.getWriter()
                .println("HTTP Status 401 - " + authenticationException.getMessage());
    }

    @Override
    @SneakyThrows
    public void afterPropertiesSet() {
        setRealmName("SDA");
        super.afterPropertiesSet();
    }

}