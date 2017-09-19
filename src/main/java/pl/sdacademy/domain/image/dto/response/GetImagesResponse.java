package pl.sdacademy.domain.image.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Value
@Builder
@RequiredArgsConstructor
public class GetImagesResponse {
    private final Long id;
    private final String name;
    private final String owner;
    private final Boolean isPublic;
    private final List<String> tags;
    @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Date created;
}
