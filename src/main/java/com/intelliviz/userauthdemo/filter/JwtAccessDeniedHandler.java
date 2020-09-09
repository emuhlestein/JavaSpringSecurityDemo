package com.intelliviz.userauthdemo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelliviz.userauthdemo.constant.SecurityConstant;
import com.intelliviz.userauthdemo.domain.HttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        HttpResponse resp = new HttpResponse(FORBIDDEN.value(), FORBIDDEN,
                FORBIDDEN.getReasonPhrase().toUpperCase(), SecurityConstant.ACCESS_DENIED_MESSAGE);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, resp);
        outputStream.flush();
    }
}
