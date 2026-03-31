package com.biddingmate.biddinggo.common.filter;

import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisConnectionException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class RedisConnectionExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (!isRedisConnectivityException(e)) {
                throw e;
            }

            if (response.isCommitted()) {
                throw e;
            }

            ErrorType errorType = ErrorType.REDIS_UNAVAILABLE;
            ApiResponse<Void> body = ApiResponse.<Void>builder()
                    .status(errorType.getHttpStatus().value())
                    .errorCode(errorType.getErrorCode())
                    .message(errorType.getMessage())
                    .result(null)
                    .build();

            response.resetBuffer();
            response.setStatus(errorType.getHttpStatus().value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(body));
            response.flushBuffer();

            log.error("[redis-unavailable] uri={} message={}", request.getRequestURI(), e.getMessage());
        }
    }

    private boolean isRedisConnectivityException(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof RedisConnectionFailureException
                    || current instanceof RedisSystemException
                    || current instanceof DataAccessResourceFailureException
                    || current instanceof RedisConnectionException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
