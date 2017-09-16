package pl.sdacademy.domain.shared;

import pl.sdacademy.domain.shared.exceptions.BaseApiException;
import pl.sdacademy.domain.shared.exceptions.InternalServer500Exception;
import rx.functions.Action1;

public class RxJavaCommonFunctions {
    public static Action1<Throwable> throwCustomExceptionOrElse(ApiStatus apiStatus) {
        return e -> {
            if (e instanceof BaseApiException) {
                throw (BaseApiException) e;
            }
            throw new InternalServer500Exception(apiStatus, e);
        };
    }
}
