package id.itsmeyaw.spotifyoverlay.service;

import id.itsmeyaw.spotifyoverlay.dto.SpotifyActivity;
import id.itsmeyaw.spotifyoverlay.repository.SpotifyActivitiesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SpotifyActivityService {
    private final SpotifyActivitiesRepository spotifyActivitiesRepository;

    public SpotifyActivityService(
            SpotifyActivitiesRepository spotifyActivitiesRepository
    ) {
        this.spotifyActivitiesRepository = spotifyActivitiesRepository;
    }

    public SpotifyActivity newSpotifyActivity(String userId, String userAccessToken) {
        SpotifyActivity activity = this.spotifyActivitiesRepository.findByUserId(userId);

        if (activity != null) {
            activity.setUserAccessToken(userAccessToken);
            return activity;
        } else {
            return this.spotifyActivitiesRepository.save(new SpotifyActivity(userId, userAccessToken));
        }
    }

    public SpotifyActivity getSpotifyActivity(String secret) {
        return this.spotifyActivitiesRepository.findById(secret).orElse(null);
    }

    public String getSpotifyPlaybackData(String secret) {
        Optional<SpotifyActivity> activityOptional = this.spotifyActivitiesRepository.findById(secret);

        if (activityOptional.isEmpty()) {
            return null;
        } else {
            SpotifyActivity activity = activityOptional.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(activity.getUserAccessToken());

            HttpEntity<String> request = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/me/player",
                    HttpMethod.GET,
                    request,
                    String.class
            );

            return switch (response.getStatusCode().value()) {
                case 200 -> response.getBody();
                case 204 -> {
                    log.warn("204 Error: Playback not available");
                    yield null;
                }
                case 401 -> {
                    log.error("401 Error: User {} needs to re-authenticate", activity.getUserId());
                    yield null;
                }
                case 403 -> {
                    log.error("403 Error: Bad OAuth request with request: {}", request);
                    yield null;
                }
                case 429 -> {
                    log.error("429 Rate limited");
                    yield null;
                }
                default -> {
                    log.error("Unknown case: {}", response.getStatusCode().value());
                    yield null;
                }
            };
        }
    }
}
