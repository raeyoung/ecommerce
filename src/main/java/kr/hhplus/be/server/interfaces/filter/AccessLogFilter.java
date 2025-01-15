package kr.hhplus.be.server.interfaces.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
public class AccessLogFilter implements Filter {

    // Proxy 서버를 통해 들어온 경우, 클라이언트의 IP 주소를 추출하기 위한 헤더 목록
    private static final String[] headers = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        log.info("[REQUEST] URI: {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());

        long startTime = System.currentTimeMillis();

        chain.doFilter(request, response);

        log.info("[RESPONSE] STATUS: {} ({}ms)", httpServletResponse.getStatus(), (System.currentTimeMillis() - startTime));
    }

    private String extractClientIp(HttpServletRequest request) {
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
