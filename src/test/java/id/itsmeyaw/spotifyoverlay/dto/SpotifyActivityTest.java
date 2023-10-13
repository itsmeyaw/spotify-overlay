package id.itsmeyaw.spotifyoverlay.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpotifyActivityTest {
    @Test
    public void testSimpleInvocation() {
        SpotifyActivity sut = new SpotifyActivity("testUserId", "testUserAccessToken");

        assertEquals(sut.getUserId(), "testUserId");
        assertEquals(sut.getUserAccessToken(), "testUserAccessToken");
        assertTrue(sut.getSecretUrl().length() > 20);
        System.out.println("Secure URL is: " + sut.getSecretUrl());
    }
}
