package com.bob.projects.twoFactorAuth.config;

import com.bob.projects.twoFactorAuth.service.JwtUtil;
import com.bob.projects.twoFactorAuth.service.InstaUserDetails;
import com.bob.projects.twoFactorAuth.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private JwtUtil jwtUtil;
    private UserService userService;

    public JwtAuthenticationFilter(
            JwtConfig jwtConfig,
            JwtUtil jwtUtil,
            UserService userService) {
        this.jwtConfig = jwtConfig;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(jwtConfig.getHeader());
        if(header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace(jwtConfig.getPrefix(), "");
        if(jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getClaimsFromJWT(token);
            String username = claims.getSubject();
            UsernamePasswordAuthenticationToken auth =
                    userService.findByUsername(username)
                            .map(InstaUserDetails::new)
                            .map(userDetails -> {
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities());
                                authentication
                                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                return authentication;
                            })
                            .orElse(null);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

}
