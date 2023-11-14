package id.itsmeyaw.spotifyoverlay.repository;

import id.itsmeyaw.spotifyoverlay.dto.SpotifyActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotifyActivitiesRepository extends JpaRepository<SpotifyActivity, String> {
    SpotifyActivity findByUserId(String userId);
}
