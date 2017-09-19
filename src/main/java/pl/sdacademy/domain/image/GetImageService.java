package pl.sdacademy.domain.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.authorization.AuthorizationFacade;
import pl.sdacademy.domain.image.dto.response.GetImagesResponse;
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;
import rx.Observable;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static pl.sdacademy.domain.shared.ApiStatus.*;
import static pl.sdacademy.domain.shared.RxJavaCommonFunctions.throwCustomExceptionOrElse;
import static rx.Observable.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetImageService {

    private final AuthorizationFacade authorizationFacade;
    private final ImageRepository imageRepository;
    private final ImageStorage imageStorage;
    private final ImageTagRepository imageTagRepository;

    public Observable<Resource> getImage(Long imageId) {
        return just(imageId).map(imageRepository::findOne)
                            .filter(Objects::nonNull)
                            .switchIfEmpty(error(new NotFound404Exception(IMAGE_NOT_FOUND)))
                            .filter(this::canBeDownloaded)
                            .switchIfEmpty(error(new Forbidden403Exception(IMAGE_NOT_ACCESSIBLE)))
                            .map(Image::getFileName)
                            .map(imageStorage::loadAsResource);
    }

    public Observable<Page<GetImagesResponse>> getImages(Pageable pageable, List<String> tags) {
        return zip(just(tags), authorizationFacade.getByToken(), just(pageable),
                   imageRepository::findAllAccessibleByUserByPageAndTags)
                .map(images -> images.map(this::mapToResponse));
    }

    private GetImagesResponse mapToResponse(Image image) {
        return GetImagesResponse.builder()
                                .id(image.getId())
                                .name(image.getOriginalFileName())
                                .created(image.getCreatedDate())
                                .owner(image.getUser()
                                            .getUsername())
                                .isPublic(image.getIsPublic())
                                .tags(imageTagRepository.findAllByImages(image)
                                                        .stream()
                                                        .map(ImageTag::getName)
                                                        .collect(toList()))
                                .build();
    }

    private Boolean canBeDownloaded(Image image) {
        return authorizationFacade.getByToken()
                                  .map(image.getUser()::equals)
                                  .zipWith(just(image).map(Image::getIsPublic),
                                           Boolean::logicalOr)
                                  .doOnError(throwCustomExceptionOrElse(INTERNAL_SERVER_ERROR))
                                  .toBlocking()
                                  .single();
    }
}
