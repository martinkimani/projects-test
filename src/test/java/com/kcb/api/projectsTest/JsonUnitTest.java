package com.kcb.api.projectsTest;

    
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JsonUnitTest {

    @Test
    void testValidJsonPayload() {
        String payload = """
            {
                "name": "",
                "description": "This is the first projects"
            }
            """;

        try {
            // Parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            // Assertions
            assertNotNull(jsonNode, "JSON should not be null");
            assertTrue(jsonNode.isObject(), "Should be a JSON object");
            
            // Check specific fields
            assertEquals("", jsonNode.get("name").asText(), "Name should be an empty string");
            assertEquals("This is the first projects", jsonNode.get("description").asText(), "Description should match");
            
            // Verify number of fields
            assertEquals(2, jsonNode.size(), "Should have exactly 2 fields");

        } catch (Exception e) {
            fail("JSON parsing failed: " + e.getMessage());
        }
    }

}

