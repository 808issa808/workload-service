package org.epam.web.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String headers = getHeadersAsString(request);
        log.debug("[REQUEST] Method: {} | URI: {} | Headers: {}",
                request.getMethod(), request.getRequestURI(), headers);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (response.getStatus() >= 400) {
            log.warn("[RESPONSE] Status: {} | URI: {} | Method: {}",
                    response.getStatus(), request.getRequestURI(), request.getMethod());
        } else {
            log.info("[RESPONSE] Status: {} | URI: {} | Method: {}",
                    response.getStatus(), request.getRequestURI(), request.getMethod());
        }

        if (ex != null) {
            log.error("[ERROR] URI: {} | Exception: {}", request.getRequestURI(), ex.getMessage(), ex);
        }
    }

    private String getHeadersAsString(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        return headerNames != null
                ? Collections.list(headerNames).stream()
                .map(name -> name + "=" + request.getHeader(name))
                .collect(Collectors.joining(", "))
                : "No headers";
    }
}