package sesohaeng.sesohaengbackend.service.feed;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sesohaeng.sesohaengbackend.domain.feed.Feed;
import sesohaeng.sesohaengbackend.domain.feed.FeedRepository;
import sesohaeng.sesohaengbackend.domain.feedimage.FeedImage;
import sesohaeng.sesohaengbackend.domain.feedimage.FeedImageRepository;
import sesohaeng.sesohaengbackend.domain.heart.Heart;
import sesohaeng.sesohaengbackend.domain.heart.HeartRepository;
import sesohaeng.sesohaengbackend.domain.place.Place;
import sesohaeng.sesohaengbackend.domain.place.PlaceRepository;
import sesohaeng.sesohaengbackend.domain.user.User;
import sesohaeng.sesohaengbackend.domain.user.UserRepository;
import sesohaeng.sesohaengbackend.dto.response.FileRequestDto;
import sesohaeng.sesohaengbackend.exception.NoDataException;
import sesohaeng.sesohaengbackend.service.S3service;
import sesohaeng.sesohaengbackend.service.feed.dto.request.FeedServiceRequest;
import sesohaeng.sesohaengbackend.service.feed.dto.response.FeedListServiceResponse;
import sesohaeng.sesohaengbackend.service.feed.dto.response.FeedServiceResponse;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final FeedImageRepository feedImageRepository;
    private final HeartRepository heartRepository;

    @Autowired
    private S3service s3service;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public final FeedServiceResponse saveFeed(@Valid final FeedServiceRequest feedServiceRequest, Long userId, MultipartFile image) {
        logger.info("피드 생성");

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoDataException("user가 존재하지 않습니다."));
        Place place = placeRepository.findByPlaceName(feedServiceRequest.getPlaceName());

        Feed feed = feedRepository.save(Feed.newInstance(
                feedServiceRequest.getContent(),
                user,
                place
        ));

        FeedImage feedImage = null;
        if(!image.isEmpty()) {
            FileRequestDto storedFile = s3service.upload(image);
            feedImage = feedImageRepository.save(FeedImage.newInstance(storedFile.getImageUrl(), feed));
        }

        return convertFeedResponse(feed, feedImage);
    }

    public final FeedListServiceResponse getFeeds() {
        logger.info("피드 리스트");

        List<Feed> feeds = feedRepository.findAll();

        return FeedListServiceResponse.newInstance(
                feeds.stream().map(feed -> convertFeedResponse(feed, feedImageRepository.findByFeed(feed))).collect(Collectors.toList())
        );
    }

    public final FeedServiceResponse getFeed(final Long id) {
        logger.info("피드 상세 페이지");

        Feed feed = feedRepository.findById(id).orElseThrow(() -> new NoDataException("피드가 존재하지 않습니다."));

        return convertFeedResponse(feed, feedImageRepository.findByFeed(feed));
    }

    public final FeedServiceResponse updateFeed(final Long id, @Valid final FeedServiceRequest feedServiceRequest) {
        logger.info("피드 수정");

        Feed feed = feedRepository.findById(id).orElseThrow(() -> new NoDataException("피드가 존재하지 않습니다."));
        Place place = placeRepository.findByPlaceName(feedServiceRequest.getPlaceName());
        feed.setContent(feedServiceRequest.getContent());
        feed.setPlace(place);

        Feed modifyFeed = feedRepository.save(feed);

        return convertFeedResponse(modifyFeed, feedImageRepository.findByFeed(modifyFeed));
    }

    public final boolean deleteFeed(final Long id) {
        feedRepository.deleteById(id);
        return true;
    }

    public final FeedListServiceResponse getMyFeeds(Long userId) {
        logger.info("내가 쓴 게시물");

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoDataException("user가 존재하지 않습니다."));

        List<Feed> feeds = feedRepository.findByUser(user);

        return FeedListServiceResponse.newInstance(
                feeds.stream().map(feed ->
                        convertFeedResponse(feed, feedImageRepository.findByFeed(feed))).collect(Collectors.toList())
        );
    }

    @Transactional
    public Integer heartFeed(final Long feedId, Long userId) {
        logger.info("좋아요 누르기");

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new NoDataException("피드가 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataException("user가 존재하지 않습니다."));

        heartRepository.save(Heart.newInstance(user, feed));

        Integer heartCount = heartRepository.countByFeedId(feedId);
        return heartCount;
    }

    @Transactional
    public Integer unheartFeed(final Long feedId, Long userId) {
        logger.info("좋아요 누르기");

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new NoDataException("피드가 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataException("user가 존재하지 않습니다."));

        heartRepository.deleteByFeedAndUser(feed, user);

        Integer heartCount = heartRepository.countByFeedId(feedId);
        return heartCount;
    }

    private FeedServiceResponse convertFeedResponse(Feed feed, FeedImage feedImage) {
        return FeedServiceResponse.of(
                feed.getId(),
                feed.getContent(),
                feed.getUser().getUsername(),
                feed.getPlace().getPlaceName(),
                feed.getCreatedAt(),
                feedImage.getImageUrl()
        );
    }
}
