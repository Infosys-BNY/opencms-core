package org.opencms.content.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;
import org.opencms.content.dto.ContentEntityDTO;
import org.opencms.content.exception.ContentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class XmlContentParser {
    
    private static final Logger LOG = LoggerFactory.getLogger(XmlContentParser.class);
    
    public static Document parseXml(byte[] xmlBytes) {
        try {
            SAXReader reader = new SAXReader();
            return reader.read(new ByteArrayInputStream(xmlBytes));
        } catch (DocumentException e) {
            LOG.error("Failed to parse XML content", e);
            throw new ContentServiceException("Failed to parse XML content: " + e.getMessage(), e);
        }
    }
    
    public static byte[] serializeXml(Document document) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            writer.close();
            return out.toByteArray();
        } catch (Exception e) {
            LOG.error("Failed to serialize XML content", e);
            throw new ContentServiceException("Failed to serialize XML content: " + e.getMessage(), e);
        }
    }
    
    public static ContentEntityDTO documentToEntity(Document document, String entityId, String locale) {
        Element root = document.getRootElement();
        String typeName = root.getName();
        
        ContentEntityDTO entity = new ContentEntityDTO(entityId, typeName);
        
        Element localeElement = findLocaleElement(root, locale);
        if (localeElement != null) {
            parseElement(localeElement, entity);
        }
        
        return entity;
    }
    
    private static Element findLocaleElement(Element root, String locale) {
        List<Element> children = root.elements();
        for (Element child : children) {
            String lang = child.attributeValue("language");
            if (locale.equals(lang)) {
                return child;
            }
        }
        return children.isEmpty() ? null : children.get(0);
    }
    
    private static void parseElement(Element element, ContentEntityDTO entity) {
        List<Element> children = element.elements();
        for (Element child : children) {
            String name = child.getName();
            if (child.elements().isEmpty()) {
                String value = child.getTextTrim();
                if (value != null && !value.isEmpty()) {
                    entity.addSimpleAttribute(name, value);
                }
            } else {
                ContentEntityDTO nestedEntity = new ContentEntityDTO(null, name);
                parseElement(child, nestedEntity);
                entity.addEntityAttribute(name, nestedEntity);
            }
        }
    }
}
