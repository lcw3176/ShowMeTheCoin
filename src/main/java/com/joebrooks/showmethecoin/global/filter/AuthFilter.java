package com.joebrooks.showmethecoin.global.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
                    log.info("미인증 사용자 요청: {}, {}", requestURI, httpRequest.getRemoteAddr());
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

}

