package com.instrospect.jwt_validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes para verificar se a documentação OpenAPI está funcionando corretamente.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnOpenApiDocumentation() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title").value("JWT Validator API"))
                .andExpect(jsonPath("$.info.description").value("API para validação de tokens JWT baseada em regras customizadas"))
                .andExpect(jsonPath("$.info.version").value("1.0.0"));
    }

    @Test
    public void shouldReturnSwaggerUiRedirect() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldReturnSwaggerUiIndex() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html"));
    }

    @Test
    public void shouldContainJwtValidationEndpointsInDocumentation() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/jwt/validate']").exists())
                .andExpect(jsonPath("$.paths['/api/jwt/extract-claims']").exists())
                .andExpect(jsonPath("$.paths['/api/jwt/validate'].post").exists())
                .andExpect(jsonPath("$.paths['/api/jwt/extract-claims'].post").exists());
    }

    @Test
    public void shouldContainJwtRequestAndValidationResponseSchemas() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components").exists())
                .andExpect(jsonPath("$.components.schemas").exists());
    }
}
