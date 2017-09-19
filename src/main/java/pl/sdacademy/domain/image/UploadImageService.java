package pl.sdacademy.domain.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.sdacademy.domain.authorization.AuthorizationFacade;
import pl.sdacademy.domain.image.dto.response.UploadImageResponse;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception;
import pl.sdacademy.domain.user.User;
import pl.sdacademy.domain.verification.EmailVerificationFacade;
import rx.Observable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;
import static pl.sdacademy.domain.shared.ApiStatus.*;
import static pl.sdacademy.domain.shared.RxJavaCommonFunctions.throwCustomExceptionOrElse;
import static rx.Observable.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadImageService {

    private final ImageStorage imageStorage;
    private final ImageFileNameGenerator fileNameGenerator;
    private final ImageRepository imageRepository;
    private final AuthorizationFacade authorizationFacade;
    private final ImageTagRepository imageTagRepository;
    private final EmailVerificationFacade emailVerificationFacade;

    public Observable<UploadImageResponse> uploadImage(MultipartFile file, Boolean isPublic, List<String> tags) {
        String fileName = fileNameGenerator.generate();

        saveFileToStorage(file, fileName);

        return zip(authorizationFacade.getByToken()
                                      .filter(emailVerificationFacade::hasVerifiedEmail)
                                      .switchIfEmpty(error(new Forbidden403Exception(USER_IS_NOT_VERIFIED))),
                   just(file), just(fileName), just(isPublic),
                   this::createImageFrom)
                .map(imageRepository::save)
                .doOnNext(image -> zip(just(image), just(tags), this::createTagsBasedOn)
                        .subscribe(imageTagRepository::save))
                .map(Image::getId)
                .map(UploadImageResponse::new);
    }

    private Image createImageFrom(User user, MultipartFile file, String fileName, Boolean isPublic) {
        return Image.builder()
                    .originalFileName(file.getOriginalFilename())
                    .fileName(fileName)
                    .isPublic(isPublic)
                    .user(user)
                    .createdDate(Date.from(now()))
                    .build();
    }

    private void saveFileToStorage(MultipartFile file, String fileName) {
        zip(just(file).filter(f -> f.getContentType()
                                    .split("/")[0].equals("image"))
                      .switchIfEmpty(error(new BadRequest400Exception(FILE_IS_NOT_AN_IMAGE))),
            just(fileName),
            imageStorage::store)
                .toBlocking()
                .subscribe(length -> log.info("Written {} bytes", length),
                           throwCustomExceptionOrElse(INTERNAL_SERVER_ERROR));
    }

    private List<ImageTag> createTagsBasedOn(Image image, List<String> tags) {
        return tags.stream()
                   .map(tag -> createTagBasedOn(image, tag))
                   .collect(toList());

    }

    private ImageTag createTagBasedOn(Image image, String tagName) {
        ImageTag imageTag = imageTagRepository.findByName(tagName)
                                              .orElseGet(() -> ImageTag.builder()
                                                                       .name(tagName)
                                                                       .images(new ArrayList<>())
                                                                       .build());
        imageTag.getImages()
                .add(image);
        return imageTag;
    }

}
