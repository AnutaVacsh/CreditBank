package ru.vaschenko.gateway.controller.mvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.vaschenko.gateway.client.deal.DealFacade;
import ru.vaschenko.gateway.controller.DocumentController;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DocumentControllerMvcTest {

    @Mock
    private DealFacade dealClient;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
    }

    @Test
    void sendCodeDocument_callsDealFacade() throws Exception {
        // Given
        UUID statementId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(post("/v1/document/{statementId}/send", statementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void signCodeDocument_callsDealFacade() throws Exception {
        // Given
        UUID statementId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(post("/v1/document/{statementId}/sign", statementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void codeDocument_callsDealFacade() throws Exception {
        // Given
        UUID statementId = UUID.randomUUID();
        String sesCode = "12345";

        // When & Then
        mockMvc.perform(post("/v1/document/{statementId}/code", statementId)
                        .param("sesCode", sesCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
