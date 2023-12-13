package tacos.security;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
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
            // Existing configuration
            .authorizeRequests(authorizeRequests -> {
                authorizeRequests
                    // Protect POST requests to /api/ingredients with the writeIngredients scope
                    .requestMatchers(new AntPathRequestMatcher("/api/ingredients", "POST"))
                    .hasAuthority("SCOPE_writeIngredients")
                    // Secure GET requests to /api/ingredients (adjust authority as needed)
                    .requestMatchers(new AntPathRequestMatcher("/api/ingredients", "GET"))
                    .authenticated() // or .hasAuthority("SCOPE_yourScope") if a specific scope is required
                    // Existing matchers
                    .requestMatchers(new AntPathRequestMatcher("/design", "GET")).hasRole("USER")
                    .requestMatchers(new AntPathRequestMatcher("/orders", "GET")).hasRole("USER")
                    .requestMatchers(new AntPathRequestMatcher("/data-api/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/**")).permitAll();
            })
            // Continue with the existing formLogin, oauth2Login, logout, csrf, and headers configurations
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/design", true)
                    .permitAll()
            )
            .oauth2Login(oauth2Login ->
                oauth2Login
                    .loginPage("/login")
                    .defaultSuccessUrl("/design", true)
                    .failureHandler(authenticationFailureHandler())
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer
                    .jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                    )
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/?logout")
            )
            .csrf().disable()
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

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // Configure the converter if needed
        return converter;
    }

    }


    