package pl.sdacademy.domain.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sdacademy.domain.authorization.AuthorizationFacade;
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;
import pl.sdacademy.domain.verification.EmailVerificationFacade;

import java.util.Objects;

import static pl.sdacademy.domain.shared.ApiStatus.*;
import static pl.sdacademy.domain.shared.RxJavaCommonFunctions.throwCustomExceptionOrElse;
import static rx.Observable.error;
import static rx.Observable.just;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteImageService {

    private final ImageRepository imageRepository;
    private final AuthorizationFacade authorizationFacade;
    private final EmailVerificationFacade emailVerificationFacade;

    public void deleteImage(Long imageId) {
        authorizationFacade.getByToken()
                           .filter(emailVerificationFacade::hasVerifiedEmail)
                           .switchIfEmpty(error(new Forbidden403Exception(USER_IS_NOT_VERIFIED)))
                           .zipWith(just(imageId), imageRepository::findAccessibleByUserById)
                           .filter(Objects::nonNull)
                           .switchIfEmpty(error(new NotFound404Exception(IMAGE_NOT_FOUND)))
                           .map(this::anonymizeImage)
                           .toBlocking()
                           .subscribe(imageRepository::save,
                                      throwCustomExceptionOrElse(INTERNAL_SERVER_ERROR));
    }

    private Image anonymizeImage(Image image) {
        image.setUser(null);
        image.setIsPublic(false);
        return image;
    }
}
