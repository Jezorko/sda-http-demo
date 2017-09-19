package pl.sdacademy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import pl.sdacademy.domain.image.ImageStorage;

@SpringBootApplication
@EnableAspectJAutoProxy
public class HttpDemoApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HttpDemoApplication.class, args);
    }

    @Bean
    @Profile("!database-test")
    CommandLineRunner init(ImageStorage imageStorage) {
        return (args) -> {
            imageStorage.deleteAll();
            imageStorage.init();
        };
    }
}
