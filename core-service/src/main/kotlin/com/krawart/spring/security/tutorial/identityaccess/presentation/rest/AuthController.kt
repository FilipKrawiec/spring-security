package com.krawart.spring.security.tutorial.identityaccess.presentation.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/")
class AuthController {

    @GetMapping("login")
    fun loginPage(): ModelAndView = ModelAndView("loginPage")
}