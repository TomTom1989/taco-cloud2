package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
            .logout(logout -> logout
                    .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout")
            )
            // Other configurations...
            ;

        // Disable CSRF for H2 console
        http.csrf().disable();
        // Allow use of frame to same origin urls (needed for H2-console)
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

}
