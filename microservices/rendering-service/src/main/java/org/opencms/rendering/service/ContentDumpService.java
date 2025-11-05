package org.opencms.rendering.service;

import org.opencms.rendering.model.RenderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ContentDumpService {
    
    @Autowired
    private JspRenderingService jspRenderingService;
    
    public byte[] dump(RenderRequest request, HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        var response = jspRenderingService.render(request, req, res);
        return response.getContent().getBytes(StandardCharsets.UTF_8);
    }
}
