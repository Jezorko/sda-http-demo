package pl.sdacademy.domain.image;

import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
public class ImageFileNameGenerator {
    String generate() {
        return randomUUID().toString();
    }
}
