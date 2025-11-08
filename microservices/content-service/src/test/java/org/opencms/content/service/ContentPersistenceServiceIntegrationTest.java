package org.opencms.content.service;

import org.dom4j.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.opencms.content.dto.ContentEntityDTO;
import org.opencms.content.dto.SaveResultDTO;
import org.opencms.content.repository.ResourceLockRepository;
import org.opencms.content.util.XmlContentParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.persistence.EntityManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContentPersistenceServiceIntegrationTest {
    
    @Autowired
    private ContentPersistenceService persistenceService;
    
    @Autowired
    private VfsOperationsServiceImpl vfsService;
    
    @Autowired
    private ResourceLockRepository resourceLockRepository;
    
    @Autowired
    private EntityManager entityManager;
    
    @BeforeEach
    void setUp() {
        resourceLockRepository.deleteAll();
        resourceLockRepository.flush();
        entityManager.clear();
    }
    
    private static final UUID TEST_STRUCTURE_ID_1 = UUID.fromString("a1b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d");
    private static final UUID TEST_STRUCTURE_ID_2 = UUID.fromString("a2b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d");
    
    @Test
    @Order(1)
    void testSaveAndDeleteEntities_persistsChanges() throws Exception {
        byte[] originalContent = vfsService.readFile(TEST_STRUCTURE_ID_1);
        Document originalDoc = XmlContentParser.parseXml(originalContent);
        ContentEntityDTO originalEntity = XmlContentParser.documentToEntity(originalDoc, 
            TEST_STRUCTURE_ID_1.toString() + "_en", "en");
        
        ContentEntityDTO modifiedEntity = new ContentEntityDTO(
            TEST_STRUCTURE_ID_1.toString() + "_en", "Article");
        modifiedEntity.addSimpleAttribute("Title", "Modified Test Title");
        modifiedEntity.addSimpleAttribute("Text", "Modified test content");
        
        SaveResultDTO result = persistenceService.saveAndDeleteEntities(
            modifiedEntity,
            "test-client",
            Collections.emptyList(),
            Collections.emptyList(),
            "en",
            true,
            false
        );
        
        assertFalse(result.isHasChangedSettings());
        
        byte[] updatedContent = vfsService.readFile(TEST_STRUCTURE_ID_1);
        Document updatedDoc = XmlContentParser.parseXml(updatedContent);
        ContentEntityDTO updatedEntity = XmlContentParser.documentToEntity(updatedDoc, 
            TEST_STRUCTURE_ID_1.toString() + "_en", "en");
        
        assertEquals("Modified Test Title", updatedEntity.getSimpleAttributes().get("Title").get(0));
        assertTrue(updatedEntity.getSimpleAttributes().containsKey("Text"));
    }
    
    @Test
    @Order(2)
    void testSaveValue_updatesSpecificField() throws Exception {
        byte[] originalContent = vfsService.readFile(TEST_STRUCTURE_ID_2);
        Document originalDoc = XmlContentParser.parseXml(originalContent);
        
        String result = persistenceService.saveValue(
            TEST_STRUCTURE_ID_2.toString(),
            "Title[1]",
            "en",
            "Updated via saveValue"
        );
        
        assertEquals("", result);
        
        byte[] updatedContent = vfsService.readFile(TEST_STRUCTURE_ID_2);
        Document updatedDoc = XmlContentParser.parseXml(updatedContent);
        ContentEntityDTO updatedEntity = XmlContentParser.documentToEntity(updatedDoc, 
            TEST_STRUCTURE_ID_2.toString() + "_en", "en");
        
        assertEquals("Updated via saveValue", updatedEntity.getSimpleAttributes().get("Title").get(0));
    }
    
    @Test
    @Order(3)
    void testSaveWithValidationErrors_rollsBack() {
        ContentEntityDTO invalidEntity = new ContentEntityDTO(
            TEST_STRUCTURE_ID_1.toString() + "_en", "Article");
        invalidEntity.addSimpleAttribute("Title", "");
        
        assertThrows(Exception.class, () -> {
            persistenceService.saveAndDeleteEntities(
                invalidEntity,
                "test-client",
                Collections.emptyList(),
                Collections.emptyList(),
                "en",
                true,
                true
            );
        });
    }
    
    @Test
    @Order(7)
    void testConcurrentSave_handlesLocking() throws Exception {
        UUID structureId = TEST_STRUCTURE_ID_1;
        
        vfsService.lockResource(structureId);
        
        ContentEntityDTO entity = new ContentEntityDTO(
            structureId.toString() + "_en", "Article");
        entity.addSimpleAttribute("Title", "Concurrent Test");
        
        assertThrows(Exception.class, () -> {
            persistenceService.saveAndDeleteEntities(
                entity,
                "test-client-2",
                Collections.emptyList(),
                Collections.emptyList(),
                "en",
                true,
                false
            );
        });
        
        vfsService.unlockResource(structureId);
    }
    
    @Test
    @Order(4)
    void testDeleteLocale_removesFromXml() throws Exception {
        byte[] originalContent = vfsService.readFile(TEST_STRUCTURE_ID_2);
        Document originalDoc = XmlContentParser.parseXml(originalContent);
        
        int originalLocaleCount = originalDoc.getRootElement().elements().size();
        assertTrue(originalLocaleCount > 0);
        
        ContentEntityDTO entity = new ContentEntityDTO(
            TEST_STRUCTURE_ID_2.toString() + "_en", "Article");
        entity.addSimpleAttribute("Title", "Keep This Title");
        
        List<String> deletedEntities = Arrays.asList(TEST_STRUCTURE_ID_2.toString() + "_de");
        
        SaveResultDTO result = persistenceService.saveAndDeleteEntities(
            entity,
            "test-client",
            deletedEntities,
            Collections.emptyList(),
            "en",
            true,
            false
        );
        
        byte[] updatedContent = vfsService.readFile(TEST_STRUCTURE_ID_2);
        Document updatedDoc = XmlContentParser.parseXml(updatedContent);
        
        int updatedLocaleCount = updatedDoc.getRootElement().elements().size();
        assertTrue(updatedLocaleCount <= originalLocaleCount);
    }
    
    @Test
    @Order(5)
    void testMultiValueAttributes_preservedCorrectly() throws Exception {
        ContentEntityDTO entity = new ContentEntityDTO(
            TEST_STRUCTURE_ID_2.toString() + "_en", "Article");
        entity.addSimpleAttribute("Title", "Multi-value Test");
        entity.addSimpleAttribute("Text", "First text value");
        entity.addSimpleAttribute("Text", "Second text value");
        entity.addSimpleAttribute("Text", "Third text value");
        
        SaveResultDTO result = persistenceService.saveAndDeleteEntities(
            entity,
            "test-client",
            Collections.emptyList(),
            Collections.emptyList(),
            "en",
            true,
            false
        );
        
        byte[] updatedContent = vfsService.readFile(TEST_STRUCTURE_ID_2);
        Document updatedDoc = XmlContentParser.parseXml(updatedContent);
        ContentEntityDTO updatedEntity = XmlContentParser.documentToEntity(updatedDoc, 
            TEST_STRUCTURE_ID_2.toString() + "_en", "en");
        
        List<String> textValues = updatedEntity.getSimpleAttributes().get("Text");
        assertNotNull(textValues);
        assertEquals(3, textValues.size());
        assertTrue(textValues.contains("First text value"));
        assertTrue(textValues.contains("Second text value"));
        assertTrue(textValues.contains("Third text value"));
    }
    
    @Test
    @Order(6)
    void testNestedEntities_savedCorrectly() throws Exception {
        ContentEntityDTO nestedEntity = new ContentEntityDTO(null, "Author");
        nestedEntity.addSimpleAttribute("Name", "John Doe");
        nestedEntity.addSimpleAttribute("Email", "john@example.com");
        
        ContentEntityDTO entity = new ContentEntityDTO(
            TEST_STRUCTURE_ID_2.toString() + "_en", "Article");
        entity.addSimpleAttribute("Title", "Nested Entity Test");
        entity.addEntityAttribute("Author", nestedEntity);
        
        SaveResultDTO result = persistenceService.saveAndDeleteEntities(
            entity,
            "test-client",
            Collections.emptyList(),
            Collections.emptyList(),
            "en",
            true,
            false
        );
        
        byte[] updatedContent = vfsService.readFile(TEST_STRUCTURE_ID_2);
        Document updatedDoc = XmlContentParser.parseXml(updatedContent);
        ContentEntityDTO updatedEntity = XmlContentParser.documentToEntity(updatedDoc, 
            TEST_STRUCTURE_ID_2.toString() + "_en", "en");
        
        assertTrue(updatedEntity.getEntityAttributes().containsKey("Author"));
        ContentEntityDTO savedNestedEntity = updatedEntity.getEntityAttributes().get("Author").get(0);
        assertEquals("John Doe", savedNestedEntity.getSimpleAttributes().get("Name").get(0));
        assertEquals("john@example.com", savedNestedEntity.getSimpleAttributes().get("Email").get(0));
    }
}
