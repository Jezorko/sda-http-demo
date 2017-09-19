package pl.sdacademy.domain.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sdacademy.domain.image.dto.response.GetImagesResponse;
import pl.sdacademy.domain.image.dto.response.UploadImageResponse;
import rx.Observable;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/images")
public class ImageController {

    private final UploadImageService uploadImageService;
    private final GetImageService getImageService;
    private final DeleteImageService deleteImageService;

    @PostMapping
    Observable<UploadImageResponse> uploadFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "public", defaultValue = "true") Boolean isPublic,
                                               @RequestParam(value = "tags", defaultValue = "") List<String> tags) {
        log.info("Incoming image upload; name: {}, tags: {}", file.getOriginalFilename(), tags);
        return uploadImageService.uploadImage(file, isPublic, tags);
    }

    @GetMapping("/{imageId}")
    Observable<Resource> getImageById(@PathVariable @NotNull Long imageId) {
        log.info("Incoming get image request for id: {}", imageId);
        return getImageService.getImage(imageId);
    }

    @GetMapping
    Observable<Page<GetImagesResponse>> getImages(Pageable pageable, @RequestParam(value = "tags", defaultValue = "") List<String> tags) {
        log.info("Incoming get image request with pagination: {}, and tags: {}", pageable, tags);
        return getImageService.getImages(pageable, tags);
    }

    @DeleteMapping("/{imageId}")
    void deleteImageById(@PathVariable @NotNull Long imageId) {
        log.info("Incoming delete image request for id: {}", imageId);
        deleteImageService.deleteImage(imageId);
    }

}
