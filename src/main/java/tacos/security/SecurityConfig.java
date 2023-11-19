package tacos.security;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UriUtils;

import tacos.AppUser;
import tacos.data.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            AppUser user = userRepo.findByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(new AntPathRequestMatcher("/design", "GET")).hasRole("USER")
                    .requestMatchers(new AntPathRequestMatcher("/orders", "GET")).hasRole("USER")
                    .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // Permit H2 console access
                    .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/design", true)
                    .permitAll()
            )
            .oauth2Login(oauth2Login ->
                oauth2Login
                    .loginPage("/login") // Use the same login page for OAuth2
                    .defaultSuccessUrl("/design", true) // Redirect after OAuth2 login success
                    .failureHandler(authenticationFailureHandler()) // Custom failure handler
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/?logout")
            )
            // Disable CSRF for H2 console
            .csrf().disable()
            // Allow use of frame to same origin URLs (needed for H2-console)
            .headers().frameOptions().sameOrigin();

        return http.build();
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            // URL-encode the exception message for query parameters
            String encodedErrorMessage = UriUtils.encodeQueryParam(exception.getMessage(), StandardCharsets.UTF_8.name());
            
            // Redirect to the login page with the encoded error message
            response.sendRedirect("/login?error=" + encodedErrorMessage);
        };
    }

    }

