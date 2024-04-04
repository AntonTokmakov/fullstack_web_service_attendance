package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.dto.RegisterMonitor;
import com.diplom.web_service_attendance.dto.ReturnRegisterMonitor;
import com.diplom.web_service_attendance.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/admin")
public class AdminController {

    private final AuthorityService authorityService;

    @GetMapping("/main")
    public String main() {

        return "admin.main";
    }

    @GetMapping("/registerMonitor")
    public String register(Model model) {
        RegisterMonitor registerMonitor = authorityService.createDefaultMonitor();
        model.addAttribute("registerMonitor", registerMonitor);
        return "admin.registerMonitor";
    }

    @PostMapping("/registerMonitor")
    public String register(@ModelAttribute("registerMonitor") ReturnRegisterMonitor registerMonitor) {

        authorityService.createMonitor(registerMonitor);

        return "admin.main";
    }

}
