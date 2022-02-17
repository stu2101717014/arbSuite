package com.example.ui.security;

import com.example.ui.security.utils.SecurityUtils;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomRequestCache extends HttpSessionRequestCache {

    /**
     * {@inheritDoc}
     *
     * If the method is considered an internal request from the framework, we skip
     * saving it.
     *
     * @see SecurityUtils#isFrameworkInternalRequest(HttpServletRequest)
     */
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtils.isFrameworkInternalRequest(request)) {
            super.saveRequest(request, response);
        }
    }
}