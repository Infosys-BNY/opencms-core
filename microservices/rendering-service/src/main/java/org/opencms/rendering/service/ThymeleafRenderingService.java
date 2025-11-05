package org.opencms.rendering.service;

import org.opencms.rendering.model.RenderRequest;
import org.opencms.rendering.model.RenderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class ThymeleafRenderingService {
    
    private final TemplateEngine templateEngine;
    
    @Autowired
    public ThymeleafRenderingService() {
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }
    
    public RenderResponse render(RenderRequest request) {
        Context context = new Context();
        if (request.getContextData() != null) {
            request.getContextData().forEach(context::setVariable);
        }
        
        String content = templateEngine.process(request.getTemplateContent(), context);
        
        RenderResponse response = new RenderResponse();
        response.setContent(content);
        response.setContentType("text/html;charset=UTF-8");
        response.setLastModified(System.currentTimeMillis());
        
        return response;
    }
}
