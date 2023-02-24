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
        // 采用oauth2.0认证，这里的loginProcessingUrl可以是一个统配url，因为我们的网站可能不只有一种oauth2.0
        // 所有的oauth2.0又是一样的处理流程，因此，可以统配的方式处理所有的授权码
        http.oauth2Login().loginProcessingUrl("/callback");
        return http.build();
    }
}
