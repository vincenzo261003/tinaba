package com.adesso.controller;

import com.adesso.service.BankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class BankingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BankingService bankingService;

    @InjectMocks
    private BankingController bankingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bankingController).build();
    }

    @Test
    void testGetBalance_Success() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("accountId", 12345678);
        mockResponse.put("balance", Map.of("available", 1500.75, "currency", "EUR"));

        when(bankingService.getBalance(12345678L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/banking/accounts/12345678/balance")
                        .header("X-API-KEY", "test-api-key")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(12345678))
                .andExpect(jsonPath("$.balance.available").value(1500.75))
                .andExpect(jsonPath("$.balance.currency").value("EUR"));
    }

    @Test
    void testGetTransactions_EmptyList() throws Exception {
        when(bankingService.getTransactions(12345678L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/banking/accounts/12345678/transactions")
                        .header("X-API-KEY", "test-api-key")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void testMakeTransfer_Success() throws Exception {
        Map<String, Object> transferResponse = new HashMap<>();
        transferResponse.put("transferId", "TRF123456");
        transferResponse.put("status", "SUCCESS");
        transferResponse.put("message", "Bonifico effettuato con successo");

        when(bankingService.makeTransfer(12345678L, Map.of("receiverName", "Mario Rossi", "amount", 250.00, "currency", "EUR")))
                .thenReturn(transferResponse);

        String jsonRequest = """
                {
                    "receiverName": "Mario Rossi",
                    "amount": 250.00,
                    "currency": "EUR"
                }
                """;

        mockMvc.perform(post("/api/banking/accounts/12345678/transfers")
                        .header("X-API-KEY", "test-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transferId").value("TRF123456"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Bonifico effettuato con successo"));
    }
}
