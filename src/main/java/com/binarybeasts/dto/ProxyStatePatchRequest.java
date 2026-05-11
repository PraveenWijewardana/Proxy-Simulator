package com.binarybeasts.dto;

import com.binarybeasts.model.ProxyState;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProxyStatePatchRequest {
    @NotNull
    private ProxyState state;
}

