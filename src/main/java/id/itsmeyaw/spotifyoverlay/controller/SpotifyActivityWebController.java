package id.itsmeyaw.spotifyoverlay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.itsmeyaw.spotifyoverlay.dto.SpotifyActivity;
import id.itsmeyaw.spotifyoverlay.exceptions.SpotifyOverlayException;
import id.itsmeyaw.spotifyoverlay.service.SpotifyActivityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class SpotifyActivityWebController {

    private final SpotifyActivityService spotifyActivityService;

    public SpotifyActivityWebController(
            SpotifyActivityService spotifyActivityService
    ) {
        this.spotifyActivityService = spotifyActivityService;
    }

    @GetMapping("/spotify")
    public String spotify(
            Model model,
            @AuthenticationPrincipal OAuth2User user,
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient spotifyClient
    ) {
        model.addAttribute("name", user.getName());

        String userId = user.getAttribute("id");
        String userAccessToken = spotifyClient.getAccessToken().getTokenValue();

        SpotifyActivity activity = spotifyActivityService.newSpotifyActivity(userId, userAccessToken);

        model.addAttribute("secret", activity.getSecret());

        return "spotify";
    }

    @GetMapping("/spotify/{secret}")
    public String spotifyActivity(Model model,
                                  @PathVariable("secret") String secret
    ) {
        SpotifyActivity activity = this.spotifyActivityService.getSpotifyActivity(secret);
        model.addAttribute("userId", activity.getUserId());
        return "spotify-secret";
    }

    @GetMapping("/spotify/{secret}/data")
    public @ResponseBody String spotifyPlaybackData(
            @PathVariable("secret") String secret,
            HttpServletResponse response
    ) throws JsonProcessingException {
        String data = null;
        try {
            data = spotifyActivityService.getSpotifyPlaybackData(secret);
        } catch (SpotifyOverlayException e) {
            response.setStatus(e.getStatusCode());
            return new ObjectMapper().writeValueAsString(e);
        }

        log.info("Got data {}", data);

        if (data == null) {
            log.warn("Cannot find spotify activity for secret {}", secret);
            response.setStatus(404);
            return null;
        }

        return data;
    }
}
