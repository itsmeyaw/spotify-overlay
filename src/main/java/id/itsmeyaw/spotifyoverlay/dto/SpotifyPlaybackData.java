package id.itsmeyaw.spotifyoverlay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyPlaybackData {
    @JsonProperty("is_playing")
    private boolean isPlaying;

    @JsonProperty("item")
    private TrackObject item;

    @JsonProperty("currently_playing_type")
    private PlayingType currentlyPlayingType;

    public enum PlayingType {
        @JsonProperty("track")
        TRACK,

        @JsonProperty("episode")
        EPISODE,

        @JsonProperty("ad")
        AD,

        @JsonProperty("unknown")
        UNKNOWN
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrackObject {
        @JsonProperty("name")
        private String name;
    }
}
