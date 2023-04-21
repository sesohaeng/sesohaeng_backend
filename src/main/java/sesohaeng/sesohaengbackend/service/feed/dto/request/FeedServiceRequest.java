package sesohaeng.sesohaengbackend.service.feed.dto.request;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedServiceRequest {
    @NotNull
    private String content;

    @NotNull
    private String userEmail;

    @NotNull
    private Long placeId;

    public static FeedServiceRequest newInstance(String content, String userEmail, Long placeId) {
        return new FeedServiceRequest(content, userEmail, placeId);
    }
}