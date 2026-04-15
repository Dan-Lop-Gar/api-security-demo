package com.example.resourceserver.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ========== ENDPOINTS PÚBLICOS ==========

    @Test
    void publicHello_DeberiaRetornarMensajePublico() throws Exception {
        mockMvc.perform(get("/public/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Este endpoint es público"));
    }

    @Test
    void publicTest_DeberiaRetornarMensajeDeTest() throws Exception {
        mockMvc.perform(get("/public/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Este endpoint es público y para tests"));
    }

    @Test
    void publicUpgrade_DeberiaRetornarMensajeDeUpgrade() throws Exception {
        mockMvc.perform(get("/public/upgrade"))
                .andExpect(status().isOk())
                .andExpect(content().string("Este endpoint es público y para upgrade"));
    }
}