package org.opencms.rendering.service;

import org.opencms.rendering.cache.FlexCacheService;
import org.opencms.rendering.model.CacheMode;
import org.opencms.rendering.model.RenderRequest;
import org.opencms.rendering.model.RenderResponse;
import org.opencms.rendering.repository.TemplateMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class JspRenderingService {
    
    private static final Logger LOG = LoggerFactory.getLogger(JspRenderingService.class);
    
    @Autowired
    private TemplateMetadataRepository metadataRepository;
    
    @Autowired
    private FlexCacheService cacheService;
    
    @Autowired
    private JspUpdateService jspUpdateService;
    
    @Value("${rendering.jsp.repository}")
    private String jspRepository;
    
    public RenderResponse render(RenderRequest request, HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        boolean streaming = request.getCacheMode() == CacheMode.STREAM;
        boolean bypass = request.getCacheMode() == CacheMode.BYPASS;
        
        RenderResponse response = new RenderResponse();
        response.setContentType("text/html;charset=UTF-8");
        
        if (bypass || streaming) {
            String target = jspUpdateService.updateJsp(request);
            response.setContent(dispatchToJsp(target, request, req, res));
            response.setCached(false);
        } else {
            String cacheKey = generateCacheKey(request);
            byte[] cachedContent = cacheService.getCachedFragment(cacheKey, "default");
            
            if (cachedContent != null) {
                response.setContent(new String(cachedContent, StandardCharsets.UTF_8));
                response.setCached(true);
            } else {
                String target = jspUpdateService.updateJsp(request);
                String content = dispatchToJsp(target, request, req, res);
                response.setContent(content);
                response.setCached(false);
                
                cacheService.putFragment(cacheKey, "default", content.getBytes(StandardCharsets.UTF_8));
            }
        }
        
        return response;
    }
    
    public void include(RenderRequest request, HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        String target = jspUpdateService.updateJsp(request);
        RequestDispatcher dispatcher = req.getRequestDispatcher(target);
        dispatcher.include(req, res);
    }
    
    private String dispatchToJsp(String jspPath, RenderRequest request, 
                                  HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        LOG.debug("Dispatching to JSP: {}", jspPath);
        return "<!-- JSP dispatch not fully implemented yet -->";
    }
    
    private String generateCacheKey(RenderRequest request) {
        return request.getTemplatePath() + (request.isOnline() ? "[online]" : "[offline]");
    }
}
