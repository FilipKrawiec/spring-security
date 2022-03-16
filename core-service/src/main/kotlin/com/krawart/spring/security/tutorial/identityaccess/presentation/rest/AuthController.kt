package com.krawart.spring.security.tutorial.identityaccess.presentation.rest

import com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody.RegisterUserRequestBody
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/")
class AuthController {

    @GetMapping("login")
    fun loginPage(): String = "loginPage"

    @GetMapping("sign-up")
    fun registrationPage(): ModelAndView = ModelAndView("registrationPage", "user", RegisterUserRequestBody())
}