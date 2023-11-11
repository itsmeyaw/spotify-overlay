package id.itsmeyaw.spotifyoverlay.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class SecretStringGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
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

        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }
}
