package org.opencms.content.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;
import org.jaxen.dom4j.Dom4jXPath;
import org.jaxen.SimpleNamespaceContext;
import org.opencms.content.dto.ContentEntityDTO;
import org.opencms.content.exception.ContentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    
    /**
     * Updates an XML document from a ContentEntityDTO, mapping the entity's attributes
     * back to XML elements within the specified locale.
     *
     * @param xmlDoc the XML document to update
     * @param entity the entity containing the changes to apply
     * @param locale the locale to update (e.g., "en", "de")
     */
    public static void updateDocumentFromEntity(Document xmlDoc, ContentEntityDTO entity, String locale) {
        Element root = xmlDoc.getRootElement();
        Element localeElement = findLocaleElement(root, locale);
        
        if (localeElement == null) {
            LOG.warn("Locale {} not found in document, creating new locale element", locale);
            localeElement = root.addElement(entity.getTypeName());
            localeElement.addAttribute("language", locale);
        }
        
        localeElement.clearContent();
        
        updateElementFromEntity(localeElement, entity);
        
        LOG.debug("Updated document from entity for locale: {}", locale);
    }
    
    /**
     * Recursively updates XML elements from entity attributes.
     *
     * @param parentElement the parent XML element to update
     * @param entity the entity with attributes to map
     */
    private static void updateElementFromEntity(Element parentElement, ContentEntityDTO entity) {
        for (Map.Entry<String, List<String>> entry : entity.getSimpleAttributes().entrySet()) {
            String attrName = entry.getKey();
            List<String> values = entry.getValue();
            
            for (String value : values) {
                Element childElement = parentElement.addElement(attrName);
                if (value != null) {
                    if (value.contains("<") || value.contains(">") || value.contains("&")) {
                        childElement.addCDATA(value);
                    } else {
                        childElement.setText(value);
                    }
                }
            }
        }
        
        for (Map.Entry<String, List<ContentEntityDTO>> entry : entity.getEntityAttributes().entrySet()) {
            String attrName = entry.getKey();
            List<ContentEntityDTO> nestedEntities = entry.getValue();
            
            for (ContentEntityDTO nestedEntity : nestedEntities) {
                Element childElement = parentElement.addElement(attrName);
                updateElementFromEntity(childElement, nestedEntity);
            }
        }
    }
    
    /**
     * Removes a locale element from the XML document.
     *
     * @param xmlDoc the XML document
     * @param locale the locale to remove
     */
    public static void removeLocale(Document xmlDoc, String locale) {
        Element root = xmlDoc.getRootElement();
        Element localeElement = findLocaleElement(root, locale);
        
        if (localeElement != null) {
            root.remove(localeElement);
            LOG.debug("Removed locale: {}", locale);
        } else {
            LOG.warn("Locale {} not found for removal", locale);
        }
    }
    
    /**
     * Updates a specific value at the given XPath within a locale.
     *
     * @param xmlDoc the XML document to update
     * @param contentPath the XPath to the element (e.g., "Title[1]" or "Text[1]/content[1]")
     * @param locale the locale to update
     * @param value the new value to set
     */
    public static void updateValueAtPath(Document xmlDoc, String contentPath, String locale, String value) {
        try {
            Element root = xmlDoc.getRootElement();
            Element localeElement = findLocaleElement(root, locale);
            
            if (localeElement == null) {
                throw new ContentServiceException("Locale not found: " + locale);
            }
            
            org.jaxen.XPath xpath = new Dom4jXPath(contentPath);
            xpath.setNamespaceContext(new SimpleNamespaceContext(Collections.emptyMap()));
            
            Object result = xpath.selectSingleNode(localeElement);
            Element targetElement;
            
            if (result == null) {
                targetElement = createElementAtPath(localeElement, contentPath);
            } else if (result instanceof Element) {
                targetElement = (Element) result;
            } else {
                throw new ContentServiceException("XPath does not point to an element: " + contentPath);
            }
            
            targetElement.clearContent();
            if (value != null) {
                if (value.contains("<") || value.contains(">") || value.contains("&")) {
                    targetElement.addCDATA(value);
                } else {
                    targetElement.setText(value);
                }
            }
            
            LOG.debug("Updated value at path: {} for locale: {}", contentPath, locale);
            
        } catch (Exception e) {
            LOG.error("Failed to update value at path: {}", contentPath, e);
            throw new ContentServiceException("Failed to update value at path: " + contentPath + " - " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates an element at the specified XPath if it doesn't exist.
     *
     * @param parent the parent element
     * @param path the XPath (e.g., "Title[1]" or "Text[1]/content[1]")
     * @return the created or found element
     */
    private static Element createElementAtPath(Element parent, String path) {
        String[] parts = path.split("/");
        Element current = parent;
        
        for (String part : parts) {
            String elementName = part.replaceAll("\\[\\d+\\]", "");
            int index = extractIndex(part);
            
            List<Element> existing = current.elements(elementName);
            if (existing.size() > index) {
                current = existing.get(index);
            } else {
                while (existing.size() <= index) {
                    current = current.addElement(elementName);
                    existing = current.getParent().elements(elementName);
                }
            }
        }
        
        return current;
    }
    
    /**
     * Extracts the index from an XPath part (e.g., "Title[1]" returns 0).
     *
     * @param pathPart the path part
     * @return the zero-based index
     */
    private static int extractIndex(String pathPart) {
        if (pathPart.contains("[") && pathPart.contains("]")) {
            String indexStr = pathPart.substring(pathPart.indexOf("[") + 1, pathPart.indexOf("]"));
            return Integer.parseInt(indexStr) - 1;
        }
        return 0;
    }
}
