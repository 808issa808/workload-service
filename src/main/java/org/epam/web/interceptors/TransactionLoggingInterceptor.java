package org.epam.web.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class TransactionLoggingInterceptor implements HandlerInterceptor {
    private static final String TRANSACTION_ID = "transactionId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        var transactionId = UUID.randomUUID().toString();
        MDC.put(TRANSACTION_ID, transactionId);
        request.setAttribute(TRANSACTION_ID, transactionId);

        log.debug("[TRANSACTION START] ID: {} | URI: {} | Method: {}",
                transactionId, request.getRequestURI(), request.getMethod());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String transactionId = (String) request.getAttribute(TRANSACTION_ID);

        if (ex != null) {
            log.error("[TRANSACTION ERROR] ID: {} | URI: {} | Exception: {}",
                    transactionId, request.getRequestURI(), ex.getMessage(), ex);
        } else {
            log.info("[TRANSACTION COMPLETE] ID: {} | URI: {} | Status: {}",
                    transactionId, request.getRequestURI(), response.getStatus());
        }

        MDC.clear();
    }
}