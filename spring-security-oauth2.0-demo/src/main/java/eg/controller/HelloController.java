package eg.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    public DefaultOAuth2User index() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (DefaultOAuth2User) context.getAuthentication().getPrincipal();
    }
}
