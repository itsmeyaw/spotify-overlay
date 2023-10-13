package id.itsmeyaw.spotifyoverlay.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers("/spotify/*").permitAll()
                                .requestMatchers("/spotify").authenticated()
                                .anyRequest().permitAll()
                ).oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
