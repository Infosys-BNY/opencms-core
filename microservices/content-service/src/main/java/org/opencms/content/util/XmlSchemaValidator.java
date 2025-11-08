package org.opencms.content.util;

import org.dom4j.Document;
import org.dom4j.io.DocumentSource;
import org.opencms.content.dto.ValidationMessageDTO;
import org.opencms.content.dto.ValidationResultDTO;
import org.opencms.content.exception.ContentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.util.HashMap;

@Component
public class XmlSchemaValidator {
    
    private static final Logger LOG = LoggerFactory.getLogger(XmlSchemaValidator.class);
    
    /**
     * Validates an XML document against an XSD schema.
     *
     * @param xmlDoc the XML document to validate
     * @param schemaPath the classpath path to the XSD schema (e.g., "schemas/Article.xsd")
     * @return validation result with errors and warnings
     */
    public ValidationResultDTO validateAgainstSchema(Document xmlDoc, String schemaPath) {
        ValidationResultDTO result = new ValidationResultDTO();
        
        try {
            Schema schema = loadSchema(schemaPath);
            Validator validator = schema.newValidator();
            
            ValidationErrorHandler errorHandler = new ValidationErrorHandler(result);
            validator.setErrorHandler(errorHandler);
            
            validator.validate(new DocumentSource(xmlDoc));
            
            if (result.hasErrors()) {
                LOG.warn("Schema validation failed for schema: {}", schemaPath);
            } else {
                LOG.debug("Schema validation passed for schema: {}", schemaPath);
            }
            
        } catch (Exception e) {
            LOG.error("Error during schema validation", e);
            addValidationError(result, "schema", "Schema validation error: " + e.getMessage(), null);
        }
        
        return result;
    }
    
    private Schema loadSchema(String schemaPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream schemaStream = getClass().getClassLoader().getResourceAsStream(schemaPath);
            
            if (schemaStream == null) {
                throw new ContentServiceException("Schema not found: " + schemaPath);
            }
            
            return factory.newSchema(new StreamSource(schemaStream));
            
        } catch (SAXException e) {
            LOG.error("Failed to load schema: {}", schemaPath, e);
            throw new ContentServiceException("Failed to load schema: " + schemaPath, e);
        }
    }
    
    private void addValidationError(ValidationResultDTO result, String fieldPath, String message, String help) {
        if (!result.getErrors().containsKey("document")) {
            result.getErrors().put("document", new HashMap<>());
        }
        result.getErrors().get("document").put(fieldPath, new ValidationMessageDTO(message, help));
    }
    
    private static class ValidationErrorHandler implements org.xml.sax.ErrorHandler {
        private final ValidationResultDTO result;
        
        public ValidationErrorHandler(ValidationResultDTO result) {
            this.result = result;
        }
        
        @Override
        public void error(SAXParseException e) {
            addError("Line " + e.getLineNumber() + ", Column " + e.getColumnNumber() + ": " + e.getMessage());
        }
        
        @Override
        public void fatalError(SAXParseException e) {
            addError("FATAL - Line " + e.getLineNumber() + ": " + e.getMessage());
        }
        
        @Override
        public void warning(SAXParseException e) {
            addWarning("Line " + e.getLineNumber() + ": " + e.getMessage());
        }
        
        private void addError(String message) {
            if (!result.getErrors().containsKey("document")) {
                result.getErrors().put("document", new HashMap<>());
            }
            result.getErrors().get("document").put("validation_" + System.currentTimeMillis(), 
                new ValidationMessageDTO(message, null));
        }
        
        private void addWarning(String message) {
            if (!result.getWarnings().containsKey("document")) {
                result.getWarnings().put("document", new HashMap<>());
            }
            result.getWarnings().get("document").put("validation_" + System.currentTimeMillis(), 
                new ValidationMessageDTO(message, null));
        }
    }
}
