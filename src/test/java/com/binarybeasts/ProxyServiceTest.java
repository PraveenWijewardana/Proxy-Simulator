package com.binarybeasts;

import com.binarybeasts.dto.ProxyUpsertRequest;
import com.binarybeasts.model.ProxyState;
import com.binarybeasts.service.ProxyService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProxyServiceTest {

    @Test
    void shouldSeedTenDefaultProxies() {
        ProxyService service = new ProxyService();
        assertEquals(10, service.getAllProxies().size());
        assertEquals(ProxyState.UP, service.getProxy("px-001").getState());
        assertEquals(ProxyState.UP, service.getProxy("px-010").getState());
    }

    @Test
    void shouldBulkUpsertAndGenerateAlerts() {
        ProxyService service = new ProxyService();

        ProxyUpsertRequest first = new ProxyUpsertRequest();
        first.setId("px-001");
        first.setState(ProxyState.DOWN);

        ProxyUpsertRequest second = new ProxyUpsertRequest();
        second.setId("px-777");
        second.setState(ProxyState.TIMEOUT);

        service.bulkUpsert(List.of(first, second));

        assertEquals(11, service.getAllProxies().size());
        assertEquals(2, service.getActiveAlerts().size());
    }
}

