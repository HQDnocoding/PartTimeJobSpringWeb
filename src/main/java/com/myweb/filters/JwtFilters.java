package com.myweb.filters;

import com.myweb.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilters extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilters.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        logger.debug("Processing request: {}, Context path: {}", uri, contextPath);

        if (uri.startsWith(contextPath + "/api/secure")) {
            String header = request.getHeader("Authorization");
            logger.debug("Authorization header: {}", header);

            if (header == null || !header.startsWith("Bearer ")) {
                logger.warn("Missing or invalid Authorization header");
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            }

            String token = header.substring(7);
            try {
                JwtUtils.TokenInfo tokenInfo = JwtUtils.validateTokenAndGetInfo(token);
                String username = tokenInfo.getUsername();
                var authorities = tokenInfo.getAuthorities();
                logger.debug("Username: {}, Authorities: {}", username, authorities);

                System.out.println("Username: {}, Authorities: {}"+username+authorities );
                
                if (username != null) {
                    request.setAttribute("username", username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                logger.error("Error validating token: {}", e.getMessage());
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc hết hạn");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}