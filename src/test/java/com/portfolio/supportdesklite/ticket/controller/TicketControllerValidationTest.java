package com.portfolio.supportdesklite.ticket.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.supportdesklite.common.error.GlobalExceptionHandler;
import com.portfolio.supportdesklite.ticket.dto.CreateTicketRequest;
import com.portfolio.supportdesklite.ticket.model.TicketCategory;
import com.portfolio.supportdesklite.ticket.model.TicketPriority;
import com.portfolio.supportdesklite.ticket.service.TicketService;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TicketController.class)
@Import(GlobalExceptionHandler.class)
class TicketControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    @Test
    void rejectsInvalidCreateTicketRequest() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest(
                "bad",
                "short",
                TicketPriority.HIGH,
                TicketCategory.ACCOUNT,
                "Maria Perez",
                "not-an-email"
        );

        mockMvc.perform(post("/api/v1/tickets")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void acceptsValidCreateTicketRequestShape() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest(
                "No puedo acceder a mi cuenta",
                "El login devuelve error 500 desde el portal principal.",
                TicketPriority.HIGH,
                TicketCategory.ACCOUNT,
                "Maria Perez",
                "maria@example.com"
        );

        mockMvc.perform(post("/api/v1/tickets")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @TestConfiguration
    static class TestClockConfig {
        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2026-04-26T10:00:00Z"), ZoneOffset.UTC);
        }
    }
}
