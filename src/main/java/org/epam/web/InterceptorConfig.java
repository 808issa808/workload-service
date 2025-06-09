package org.epam.web;

import lombok.RequiredArgsConstructor;
import org.epam.web.interceptors.RequestLoggingInterceptor;
import org.epam.web.interceptors.TransactionLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final TransactionLoggingInterceptor transactionLoggingInterceptor;
    private final RequestLoggingInterceptor requestLoggingInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(transactionLoggingInterceptor)
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**"
                );

        registry.addInterceptor(requestLoggingInterceptor)
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**"
                );
    }
}