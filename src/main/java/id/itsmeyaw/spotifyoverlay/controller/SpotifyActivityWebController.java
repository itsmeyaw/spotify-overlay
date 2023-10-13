package id.itsmeyaw.spotifyoverlay.controller;

import id.itsmeyaw.spotifyoverlay.dto.SpotifyActivity;
import id.itsmeyaw.spotifyoverlay.dto.SpotifyPlaybackData;
import id.itsmeyaw.spotifyoverlay.service.SpotifyActivityService;
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

        model.addAttribute("secret", activity.getSecretUrl());

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
    public @ResponseBody SpotifyPlaybackData spotifyPlaybackData(
            @PathVariable("secret") String secret
    ) {
        SpotifyPlaybackData data = spotifyActivityService.getSpotifyPlaybackData(secret);

        log.info("Got data {}", data.toString());

        return data;
    }
}
