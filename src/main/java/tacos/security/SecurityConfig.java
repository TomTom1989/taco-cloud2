package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
        return http
            .authorizeRequests(authz -> authz
                .requestMatchers("/design", "/orders").hasRole("USER")
                .requestMatchers("/", "/**").permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login") // if you are providing a custom login page
                .defaultSuccessUrl("/design", true) // specify the default success URL after login
                .permitAll() // allows everyone to see the login page. Don't require auth for login page
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/?logout") // specify the default page after logout
                // more logout configuration
            )
            // You may want to configure CSRF, rememberMe, etc...
            .build();
    }


    }




