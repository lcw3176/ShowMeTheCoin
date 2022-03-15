package com.joebrooks.showmethecoin.global.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthFilter implements Filter {
    private static final String[] whitelist = {"/login", "/favicon.ico"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            if (isLoginCheckPath(requestURI)) {
                HttpSession session = httpRequest.getSession(false);

                if (session == null || session.getAttribute("userId") == null) {
                    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
                    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

                    log.info("\n미인증 사용자 요청" +
                                    "\n[REQUEST] {} " +
                                    "\n[PATH] {} " +
                                    "\n[IP] {}" +
                                    "\n[STATUS] {}" +
                                    "\nHeaders : {}" +
                                    "\nRequest : {}" +
                                    "\nResponse : {}\n",
                            ((HttpServletRequest) request).getMethod(),
                            ((HttpServletRequest) request).getRequestURI(),
                            httpRequest.getRemoteAddr(),
                            responseWrapper.getStatus(),
                            getHeaders((HttpServletRequest) request),
                            getRequestBody(requestWrapper),
                            getResponseBody(responseWrapper));

                    httpResponse.sendRedirect("/login");
                    return;
                }
            }

            ((HttpServletResponse) response).addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            ((HttpServletResponse) response).addHeader("Pragma", "no-cache");
            ((HttpServletResponse) response).addHeader("Expires", "0");

            chain.doFilter(request, response);

        } catch (Exception e) {
            throw e;
        }
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }


    private Map getHeaders(HttpServletRequest request) {
        Map headerMap = new HashMap<>();

        Enumeration headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = (String) headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
            }
        }
        return " - ";
    }

    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, StandardCharsets.UTF_8);
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }

}

