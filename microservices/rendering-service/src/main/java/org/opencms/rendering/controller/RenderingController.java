package org.opencms.rendering.controller;

import org.opencms.rendering.model.RenderRequest;
import org.opencms.rendering.model.RenderResponse;
import org.opencms.rendering.service.JspRenderingService;
import org.opencms.rendering.service.ThymeleafRenderingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/render")
public class RenderingController {
    
    private static final Logger LOG = LoggerFactory.getLogger(RenderingController.class);
    
    @Autowired
    private JspRenderingService jspRenderingService;
    
    @Autowired
    private ThymeleafRenderingService thymeleafRenderingService;
    
    @PostMapping("/jsp")
    public ResponseEntity<RenderResponse> renderJsp(
            @RequestBody RenderRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        try {
            LOG.debug("Rendering JSP: {}", request.getTemplatePath());
            RenderResponse response = jspRenderingService.render(request, httpRequest, httpResponse);
            return ResponseEntity.ok(response);
        } catch (ServletException | IOException e) {
            LOG.error("Error rendering JSP: {}", request.getTemplatePath(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/thymeleaf")
    public ResponseEntity<RenderResponse> renderThymeleaf(@RequestBody RenderRequest request) {
        try {
            LOG.debug("Rendering Thymeleaf: {}", request.getTemplatePath());
            RenderResponse response = thymeleafRenderingService.render(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error rendering Thymeleaf: {}", request.getTemplatePath(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
