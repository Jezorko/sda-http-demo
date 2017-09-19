package pl.sdacademy.domain.image.dto.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class UploadImageResponse {
    private final Long id;
}
