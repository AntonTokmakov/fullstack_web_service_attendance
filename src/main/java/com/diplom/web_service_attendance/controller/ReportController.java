package com.diplom.web_service_attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/report")
public class ReportController {

    @GetMapping
    public String getReport() {
        return "report";
    }

}
