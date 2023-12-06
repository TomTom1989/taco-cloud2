package tacos.security;

import java.io.IOException;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tacos.authorization.AuthUserRepository;
import tacos.authorization.User;
import tacos.data.UserRepository;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    // Redirect to the OAuth2 consent page for admins
                    String consentPageUrl = "http://localhost:9000/oauth2/authorize?response_type=code&client_id=taco-admin-client&scope=writeIngredients%20deleteIngredients";
                    getRedirectStrategy().sendRedirect(request, response, consentPageUrl);
                } else {
                    // Redirect to the default page for regular users
                    getRedirectStrategy().sendRedirect(request, response, "/design");
                }
            }
        };
    }
 
    @Bean
    UserDetailsService userDetailsService(AuthUserRepository userRepo) {
   return username -> userRepo.findByUsername(username);
    }

 



    
 
  /*  @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain basicSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create session if needed
            .and()
            .oauth2ResourceServer(oauth2 -> oauth2.jwt()) // Configure OAuth2 Resource Server with JWT
            .formLogin()
                .successHandler(successHandler()) // Custom success handler
            .and()
            .authorizeRequests(authorizeRequests -> {
                authorizeRequests
                    .requestMatchers(new AntPathRequestMatcher("/api/ingredients", "POST"))
                        .hasAuthority("SCOPE_writeIngredients")
                    .requestMatchers(new AntPathRequestMatcher("/api/ingredients", "DELETE"))
                        .hasAuthority("SCOPE_deleteIngredients")
                    .anyRequest().authenticated();
            });

        return http.build();
    }*/
    
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain basicSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Disable CSRF
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(login -> login
                        .successHandler(successHandler()))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt()) // Configure OAuth2 Resource Server with JWT
                //.and()
                .authorizeRequests(authorizeRequests -> {
                    authorizeRequests
                    .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/ingredients", "POST"))
                            .hasAuthority("SCOPE_writeIngredients")
                            .requestMatchers(new AntPathRequestMatcher("/api/ingredients", "DELETE"))
                            .hasAuthority("SCOPE_deleteIngredients")
                            .anyRequest().authenticated();
                });

        return http.build();
    }






   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
 
    @Bean
    public ApplicationRunner dataLoader(
    UserRepository repo, PasswordEncoder encoder) {
     return args -> {
     repo.save(
    new User("habuma", encoder.encode("password"), "ROLE_ADMIN"));
     repo.save(
    new User("tacochef", encoder.encode("password"), "ROLE_ADMIN"));
     };

    }
    }

