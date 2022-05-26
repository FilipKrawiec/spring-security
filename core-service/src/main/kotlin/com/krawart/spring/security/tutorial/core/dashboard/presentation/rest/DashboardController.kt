package com.krawart.spring.security.tutorial.core.dashboard.presentation.rest

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class DashboardController {

    @GetMapping
    @PreAuthorize("isAdmin()")
    fun dashboardPage(): String = "authenticated"

}