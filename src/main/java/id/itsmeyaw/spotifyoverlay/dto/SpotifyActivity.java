package id.itsmeyaw.spotifyoverlay.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Data
@Slf4j
@Entity
@NoArgsConstructor
public class SpotifyActivity {
    private String userId;

    private String userAccessToken;

    @Id
    @GeneratedValue(generator = "secret-generator")
    @GenericGenerator(name = "secret-generator", type = id.itsmeyaw.spotifyoverlay.util.SecretStringGenerator.class)
    private String secret;

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

        this.secret = Base64.getUrlEncoder().encodeToString(randomBytes);
    }
}
