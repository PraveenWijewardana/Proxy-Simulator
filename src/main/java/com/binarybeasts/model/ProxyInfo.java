package com.binarybeasts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyInfo {
    private String id;
    private ProxyState state;
    private Instant updatedAt;
}

