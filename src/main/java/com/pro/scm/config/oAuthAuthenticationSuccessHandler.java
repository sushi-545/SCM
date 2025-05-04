package com.pro.scm.config;

import com.pro.scm.entities.Providers;
import com.pro.scm.entities.User;
import com.pro.scm.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class oAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        var oauthUser= (DefaultOAuth2User) authentication.getPrincipal();
        var oauth2AuthenicationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientId= oauth2AuthenicationToken.getAuthorizedClientRegistrationId();

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of("ROLE_USER"));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("dummy");

        if (authorizedClientId.equalsIgnoreCase("google")) {

            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created using google.");

        } else if (authorizedClientId.equalsIgnoreCase("github")) {

            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
                    : oauthUser.getAttribute("login").toString() + "@gmail.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId);
            user.setProvider(Providers.GITHUB);
            user.setAbout("This account is created using github");
        }
        else {
            System.out.println("OAuthAuthenicationSuccessHandler: Unknown provider");
        }

        User userObj = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (userObj == null) {
            userRepo.save(user);
            System.out.println("user saved:" + user.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
