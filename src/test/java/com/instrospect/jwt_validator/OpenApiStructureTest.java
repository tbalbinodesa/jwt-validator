package com.instrospect.jwt_validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Teste para examinar a estrutura do JSON OpenAPI gerado.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class OpenApiStructureTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void printOpenApiJson() throws Exception {
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("[DEBUG_LOG] OpenAPI JSON Response:");
        System.out.println("[DEBUG_LOG] " + jsonResponse);
    }
}
