package com.binarybeasts.controller;

import com.binarybeasts.model.ProxyInfo;
import com.binarybeasts.model.ProxyState;
import com.binarybeasts.service.ProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProxySimulationController {

    private final ProxyService proxyService;

    @GetMapping("/proxy/{id}")
    public ResponseEntity<Map<String, Object>> simulate(@PathVariable String id) throws InterruptedException {
        ProxyInfo proxy = proxyService.getProxy(id);
        long delay = proxyService.getEffectiveDelayMs(proxy.getState());

        if (delay > 0) {
            Thread.sleep(delay);
        }

        HttpStatus status = proxy.getState() == ProxyState.DOWN
                ? HttpStatus.INTERNAL_SERVER_ERROR
                : HttpStatus.OK;

        return ResponseEntity.status(status).body(Map.of(
                "proxyId", proxy.getId(),
                "state", proxy.getState(),
                "status", status.value(),
                "delayMs", delay
        ));
    }
}

