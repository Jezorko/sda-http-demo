package pl.sdacademy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class HttpDemoApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HttpDemoApplication.class, args);
    }
}
