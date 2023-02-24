package eg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain chain(HttpSecurity http) throws Exception {
        // 所有页面都要认证
        http.authorizeHttpRequests().anyRequest().authenticated();
        // 采用oauth2.0认证
        http.oauth2Login();
        return http.build();
    }
}
