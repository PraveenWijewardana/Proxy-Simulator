package com.binarybeasts.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ConfigRequest {
    @Min(0)
    private long globalDelayMs;
}

