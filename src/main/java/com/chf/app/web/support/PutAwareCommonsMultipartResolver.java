package com.chf.app.web.support;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

public class PutAwareCommonsMultipartResolver extends StandardServletMultipartResolver {

    private static final String POST_METHOD = "POST";
    private static final String PUT_METHOD = "PUT";

    @Override
    public boolean isMultipart(HttpServletRequest request) {
        return request != null && isMultipartContent(request);
    }

    public static final boolean isMultipartContent(HttpServletRequest request) {
        String method = request.getMethod();
        if (!(POST_METHOD.equalsIgnoreCase(method) || PUT_METHOD.equalsIgnoreCase(method))) {
            return false;
        }
        return FileUploadBase.isMultipartContent(new ServletRequestContext(request));
    }
}
