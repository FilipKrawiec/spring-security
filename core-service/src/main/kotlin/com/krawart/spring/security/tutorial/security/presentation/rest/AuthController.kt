package com.krawart.spring.security.tutorial.security.presentation.rest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class AuthController {

    @GetMapping("login")
    fun loginPage(): String = "loginPage"
}