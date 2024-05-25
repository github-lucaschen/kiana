package com.lucaschen.common.web.config;

import com.lucaschen.common.core.context.SecurityContextHolder;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class GlobalMvcAutoConfig implements WebMvcConfigurer {

    private static final String TRACE_ID = "Trace-Id";

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new ThreadLocalInterceptor())
                .addPathPatterns("/**")
                .order(-100);
    }

    /**
     * 处理异步线程请求头丢失问题
     */
    public static class ThreadLocalInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final Object handler) throws Exception {
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            final Map<String, Object> headers = new HashMap<>(8);
            MDC.put(TRACE_ID, request.getHeader(TRACE_ID));

            // 放到 TransmittableThreadLocal 中，避免异步线程丢失
            SecurityContextHolder.setLocalMap(headers);
            return true;
        }

        @Override
        public void afterCompletion(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final Object handler,
                                    final Exception ex) {
            // 移除相关缓存数据，避免内溢出
            SecurityContextHolder.remove();
            MDC.remove(TRACE_ID);
        }
    }
}
