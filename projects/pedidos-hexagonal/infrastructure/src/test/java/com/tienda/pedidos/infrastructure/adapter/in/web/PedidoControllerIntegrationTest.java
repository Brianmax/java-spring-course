package com.tienda.pedidos.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"inmemory", "culqi"})
class PedidoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void flujoCompletoDeCreacionConsultaYCancelacionConReembolso() throws Exception {
        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"INT-101\",\"totalSoles\":140}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("INT-101"))
                .andExpect(jsonPath("$.estado").value("EN_PREPARACION"))
                .andExpect(jsonPath("$.reembolsado").value(false));

        mockMvc.perform(get("/api/v1/pedidos/INT-101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSoles").value(140));

        mockMvc.perform(patch("/api/v1/pedidos/INT-101/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADO"))
                .andExpect(jsonPath("$.reembolsado").value(true));
    }

    @Test
    void rechazaCancelacionDePedidoEntregadoCon409() throws Exception {
        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"INT-102\",\"totalSoles\":85}"))
                .andExpect(status().isCreated());

        mockMvc.perform(patch("/api/v1/pedidos/INT-102/entregar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));

        mockMvc.perform(patch("/api/v1/pedidos/INT-102/cancelar"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void devuelve404SiPedidoNoExisteY400SiMontoEsInvalido() throws Exception {
        mockMvc.perform(patch("/api/v1/pedidos/INT-999/cancelar"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"INT-BAD\",\"totalSoles\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
