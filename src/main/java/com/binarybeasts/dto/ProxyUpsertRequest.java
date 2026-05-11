package com.binarybeasts.dto;

import com.binarybeasts.model.ProxyState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProxyUpsertRequest {
    @NotBlank
    private String id;

    @NotNull
    private ProxyState state;
}

