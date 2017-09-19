package pl.sdacademy.domain.image;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;
import pl.sdacademy.infrastructure.properties.ApplicationProperties;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.util.FileSystemUtils.deleteRecursively;
import static pl.sdacademy.domain.shared.ApiStatus.*;

@Slf4j
@Component
public class ImageStorage {

    private final Path storagePath;

    public ImageStorage(ApplicationProperties applicationProperties) {
        storagePath = Paths.get(applicationProperties.getImagesStoragePath());
    }

    @SneakyThrows
    long store(MultipartFile file, String filename) {
        if (file.isEmpty()) {
            throw new BadRequest400Exception(FILE_IS_EMPTY);
        }
        if (filename.contains("..")) {
            throw new BadRequest400Exception(INVALID_FILE_NAME);
        }
        return Files.copy(file.getInputStream(),
                          storagePath.resolve(filename),
                          REPLACE_EXISTING);
    }

    Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new NotFound404Exception(IMAGE_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            log.info("File URL is malformed {}", e);
            throw new NotFound404Exception(IMAGE_NOT_FOUND);
        }
    }

    private Path load(String filename) {
        return storagePath.resolve(filename);
    }

    public void deleteAll() {
        deleteRecursively(storagePath.toFile());
    }

    @SneakyThrows
    public void init() {
        createDirectories(storagePath);
    }
}
