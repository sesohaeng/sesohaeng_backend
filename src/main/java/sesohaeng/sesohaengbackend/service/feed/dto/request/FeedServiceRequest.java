package sesohaeng.sesohaengbackend.service.feed.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedServiceRequest {
    @NotNull
    private String content;

    @NotNull
    private Long placeId;

    public static FeedServiceRequest newInstance(String content, Long placeId) {
        return new FeedServiceRequest(content, placeId);
    }
}
