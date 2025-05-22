package com.identlite.api.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = response.getStatus();
            String message = "{} {} -> статус: {} ({} мс)";

            if (status == HttpServletResponse.SC_BAD_REQUEST) {
                LOGGER.error(message,
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        duration);
            } else {
                LOGGER.info(message,
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        duration);
            }
        }
    }
}