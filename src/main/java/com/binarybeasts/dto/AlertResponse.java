package com.binarybeasts.dto;

import com.binarybeasts.model.ProxyState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {
    private String proxyId;
    private ProxyState state;
    private String severity;
    private String message;
}

