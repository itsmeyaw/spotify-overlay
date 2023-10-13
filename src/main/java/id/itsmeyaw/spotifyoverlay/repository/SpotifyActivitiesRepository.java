package id.itsmeyaw.spotifyoverlay.repository;

import id.itsmeyaw.spotifyoverlay.dto.SpotifyActivity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class SpotifyActivitiesRepository {
    private final HashMap<String, SpotifyActivity> activityRepository = new HashMap<>();

    public SpotifyActivity putNewSpotifyActivity(String userId, String userAccessToken) {
        SpotifyActivity spotifyActivity = new SpotifyActivity(userId, userAccessToken);
        this.activityRepository.put(spotifyActivity.getSecretUrl(), spotifyActivity);
        return spotifyActivity;
    }

    public SpotifyActivity getSpotifyActivityFromUserId(String userId) {
        return this.activityRepository.values().stream()
                .filter(activity -> userId.equals(activity.getUserId()))
                .findFirst()
                .orElse(null);
    }

    public SpotifyActivity getSpotifyActivityFromSecret(String secret) {
        return this.activityRepository.get(secret);
    }

}
