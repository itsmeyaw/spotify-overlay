package id.itsmeyaw.spotifyoverlay.exceptions;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyOverlayException extends RuntimeException {
    @JsonProperty("status")
    private final int statusCode;

    public SpotifyOverlayException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    @Override
    @JsonGetter("message")
    public String getMessage() {
        return super.getMessage();
    }
}
