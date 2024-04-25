package com.example.university.exception.service;

import com.example.university.web.constants.RequestConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Enumeration;

@Slf4j
@Component
public class ExceptionLoggingService {

    public void logRequestError(HttpServletRequest request, Exception exception) {
        String requestUri = request.getRequestURI();
        String nonAuthorizationHeaderString = parseNonAuthorizationHeader(request);
        // we don't log the actual authorization here, but we mask it with the message
        String authorizationHeaderString = parseAuthorizationHeader(request, RequestConstants.PRESENT);

        log.error(RequestConstants.LOG_FORMAT, exception.getClass().getSimpleName(), requestUri, getIpFromRequest(request), nonAuthorizationHeaderString, authorizationHeaderString, exception);
    }

    public String parseAuthorizationHeader(HttpServletRequest request, String headerMessage) {
        String header = request.getHeader(RequestConstants.AUTHORIZATION_HEADER);
        StringBuilder headerStatus = new StringBuilder(RequestConstants.AUTHORIZATION_HEADER_PREFIX);

        if (header == null) {
            headerStatus.append(RequestConstants.NULL);
        } else if (header.isEmpty()) {
            headerStatus.append(RequestConstants.EMPTY);
        } else {
            headerStatus.append(headerMessage);
        }

        return headerStatus.toString();
    }

    public String getIpFromRequest(HttpServletRequest request) {
        String ipFromHeader = request.getHeader(RequestConstants.X_FORWARDED_FOR_HEADER);

        if (StringUtils.hasText(ipFromHeader)) {
            return ipFromHeader;
        }

        return request.getRemoteAddr();
    }

    private String parseNonAuthorizationHeader(HttpServletRequest request) {
        Enumeration<String> headerEnumeration = request.getHeaderNames();
        StringBuilder headers = new StringBuilder(RequestConstants.HEADERS_PREFIX);

        while (headerEnumeration != null && headerEnumeration.hasMoreElements()) {
            String headerName = headerEnumeration.nextElement();

            if (StringUtils.hasText(headerName) && !headerName.equalsIgnoreCase(RequestConstants.AUTHORIZATION_HEADER)) {
                headers.append(String.format(RequestConstants.HEADER_KEY_VALUE, headerName, request.getHeader(headerName)));

                if (headerEnumeration.hasMoreElements()) {
                    headers.append(RequestConstants.HEADERS_DELIMITER);
                }
            }
        }
        headers.append(RequestConstants.HEADERS_SUFFIX);

        return headers.toString();
    }

}
