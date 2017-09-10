package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import pl.sdacademy.infrastructure.properties.ClientProperties;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class DemoWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final DemoBasicAuthenticationEntryPoint authenticationEntryPoint;
    private final ClientProperties clientProperties;

    @Autowired
    @SneakyThrows
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.inMemoryAuthentication()
            .withUser(clientProperties.getUsername())
            .password(clientProperties.getPassword())
            .authorities("ROLE_USER");
    }

    @Override
    @SneakyThrows
    protected void configure(HttpSecurity http) {
        http.authorizeRequests()
            .antMatchers("/tasks/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .csrf()
            .disable();
    }
}