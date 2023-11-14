package id.itsmeyaw.spotifyoverlay.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpotifyActivityTest {
    @Test
    public void testSimpleInvocation() {
        SpotifyActivity sut = new SpotifyActivity("testUserId", "testUserAccessToken");

        assertEquals(sut.getUserId(), "testUserId");
        assertEquals(sut.getUserAccessToken(), "testUserAccessToken");
        assertTrue(sut.getSecret().length() > 20);
        System.out.println("Secure URL is: " + sut.getSecret());
    }
}
