package com.binarybeasts.controller;

import com.binarybeasts.dto.ConfigRequest;
import com.binarybeasts.dto.ConfigResponse;
import com.binarybeasts.dto.AlertResponse;
import com.binarybeasts.dto.ProxyStatePatchRequest;
import com.binarybeasts.dto.ProxyUpsertRequest;
import com.binarybeasts.model.ProxyInfo;
import com.binarybeasts.service.ProxyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProxyManagementController {

    private final ProxyService proxyService;

    @PostMapping("/config")
    public ConfigResponse setConfig(@Valid @RequestBody ConfigRequest request) {
        return ConfigResponse.builder()
                .globalDelayMs(proxyService.setGlobalDelay(request.getGlobalDelayMs()).getGlobalDelayMs())
                .build();
    }

    @PostMapping("/proxies")
    public List<ProxyInfo> bulkUpsert(@Valid @RequestBody List<@Valid ProxyUpsertRequest> requests) {
        return proxyService.bulkUpsert(requests);
    }

    @PatchMapping("/proxies/{id}")
    public ProxyInfo patchProxy(@PathVariable String id, @Valid @RequestBody ProxyStatePatchRequest request) {
        return proxyService.patchProxyState(id, request.getState());
    }

    @GetMapping("/alerts")
    public List<AlertResponse> getAlerts() {
        return proxyService.getActiveAlerts();
    }

    @GetMapping("/proxies")
    public List<ProxyInfo> getProxies() {
        return proxyService.getAllProxies();
    }

    @GetMapping("/config")
    public ConfigResponse getConfig() {
        return ConfigResponse.builder().globalDelayMs(proxyService.getConfig().getGlobalDelayMs()).build();
    }
}

