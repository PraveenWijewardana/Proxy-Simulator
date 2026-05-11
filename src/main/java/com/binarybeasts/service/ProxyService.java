package com.binarybeasts.service;

import com.binarybeasts.dto.AlertResponse;
import com.binarybeasts.dto.ProxyUpsertRequest;
import com.binarybeasts.exception.ProxyNotFoundException;
import com.binarybeasts.model.ProxyInfo;
import com.binarybeasts.model.ProxyState;
import com.binarybeasts.model.SimulationConfig;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ProxyService {

    private final Map<String, ProxyInfo> proxies = new ConcurrentHashMap<>();
    private final SimulationConfig simulationConfig = SimulationConfig.builder().globalDelayMs(0).build();

    public ProxyService() {
        seedDefaults();
    }

    private void seedDefaults() {
        for (int i = 1; i <= 10; i++) {
            String id = String.format("px-%03d", i);
            proxies.put(id, ProxyInfo.builder().id(id).state(ProxyState.UP).updatedAt(Instant.now()).build());
        }
    }

    public List<ProxyInfo> getAllProxies() {
        return proxies.values().stream()
                .sorted(Comparator.comparing(ProxyInfo::getId))
                .collect(Collectors.toList());
    }

    public ProxyInfo getProxy(String id) {
        ProxyInfo proxy = proxies.get(id);
        if (proxy == null) {
            throw new ProxyNotFoundException(id);
        }
        return proxy;
    }

    public ProxyInfo upsertProxy(String id, ProxyState state) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Proxy id must not be blank");
        }
        if (state == null) {
            throw new IllegalArgumentException("Proxy state must not be null");
        }
        return proxies.compute(id, (key, existing) -> ProxyInfo.builder()
                .id(id)
                .state(state)
                .updatedAt(Instant.now())
                .build());
    }

    public List<ProxyInfo> bulkUpsert(List<ProxyUpsertRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return List.of();
        }
        List<ProxyInfo> updated = new ArrayList<>();
        for (ProxyUpsertRequest request : requests) {
            updated.add(upsertProxy(request.getId(), request.getState()));
        }
        updated.sort(Comparator.comparing(ProxyInfo::getId));
        return updated;
    }

    public ProxyInfo patchProxyState(String id, ProxyState state) {
        if (!proxies.containsKey(id)) {
            throw new ProxyNotFoundException(id);
        }
        return upsertProxy(id, state);
    }

    public SimulationConfig setGlobalDelay(long globalDelayMs) {
        if (globalDelayMs < 0) {
            throw new IllegalArgumentException("globalDelayMs must be >= 0");
        }
        simulationConfig.setGlobalDelayMs(globalDelayMs);
        return simulationConfig;
    }

    public SimulationConfig getConfig() {
        return simulationConfig;
    }

    public long getEffectiveDelayMs(ProxyState state) {
        long timeoutDelay = state == ProxyState.TIMEOUT ? 30_000L : 0L;
        return simulationConfig.getGlobalDelayMs() + timeoutDelay;
    }

    public List<AlertResponse> getActiveAlerts() {
        return getAllProxies().stream()
                .filter(proxy -> proxy.getState() == ProxyState.DOWN || proxy.getState() == ProxyState.TIMEOUT)
                .map(proxy -> AlertResponse.builder()
                        .proxyId(proxy.getId())
                        .state(proxy.getState())
                        .severity(proxy.getState() == ProxyState.DOWN ? "CRITICAL" : "WARNING")
                        .message(proxy.getState() == ProxyState.DOWN
                                ? "Proxy is unavailable"
                                : "Proxy is timing out")
                        .build())
                .toList();
    }
}

