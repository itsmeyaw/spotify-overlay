package id.itsmeyaw.spotifyoverlay.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Data
@Slf4j
public class SpotifyActivity {
    private final String userId;
    private String userAccessToken;
    private final String secretUrl;

    public SpotifyActivity(String userId, String userAccessToken) {
        this.userId = userId;
        this.userAccessToken = userAccessToken;

        SecureRandom random = new SecureRandom();

        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.warn("Cannot initiate a secure random class due to not supported algorithm, defaulting to weaker one");
        } catch (Exception e) {
            log.warn("Error in initializing secure random class due to error: {}", e.getMessage());
        }

        byte[] randomBytes = new byte[64];
        random.nextBytes(randomBytes);

        this.secretUrl = Base64.getUrlEncoder().encodeToString(randomBytes);
    }
}
