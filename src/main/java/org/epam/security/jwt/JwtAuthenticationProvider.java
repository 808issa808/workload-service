package org.epam.security.jwt;

import lombok.RequiredArgsConstructor;
import org.epam.security.LoginAttemptService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        String username = jwtService.extractUserName(token);
        List<String> roles= jwtService.extractRoles(token);

        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Account is temporarily locked");
        }

        UserDetails userDetails = createUserDetails(username,roles.toArray(roles.toArray(new String[0])));


        if (!jwtService.isTokenValid(token, userDetails)) {
            loginAttemptService.loginFailed(username);
            throw new BadCredentialsException("Invalid JWT token");
        }

        loginAttemptService.loginSucceeded(username);

        return new JwtAuthenticationToken(
                userDetails,
                token,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }

    private UserDetails createUserDetails(String username, String... roles) {
        return  org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password("")
                .roles(roles)
                .build();
    }
}