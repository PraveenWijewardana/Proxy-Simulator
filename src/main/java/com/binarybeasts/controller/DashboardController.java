package com.binarybeasts.controller;

import com.binarybeasts.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ProxyService proxyService;

    @GetMapping("/")
    public String dashboard(Model model, HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        model.addAttribute("proxies", proxyService.getAllProxies());
        model.addAttribute("config", proxyService.getConfig());
        model.addAttribute("baseUrl", baseUrl);
        return "dashboard";
    }
}

