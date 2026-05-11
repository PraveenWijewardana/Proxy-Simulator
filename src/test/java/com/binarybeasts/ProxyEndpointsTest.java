package com.binarybeasts;

import com.binarybeasts.dto.ProxyStatePatchRequest;
import com.binarybeasts.model.ProxyState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProxyEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnOkForUpProxy() throws Exception {
        mockMvc.perform(get("/proxy/px-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("UP"));
    }

    @Test
    void shouldReturn500ForDownProxy() throws Exception {
        ProxyStatePatchRequest request = new ProxyStatePatchRequest();
        request.setState(ProxyState.DOWN);

        mockMvc.perform(patch("/api/proxies/px-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/proxy/px-001"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.state").value("DOWN"));
    }
}

