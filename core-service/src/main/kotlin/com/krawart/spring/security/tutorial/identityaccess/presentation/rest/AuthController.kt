package com.krawart.spring.security.tutorial.identityaccess.presentation.rest

import com.krawart.spring.security.tutorial.identityaccess.application.UserService
import com.krawart.spring.security.tutorial.identityaccess.application.command.AddUserCommand
import com.krawart.spring.security.tutorial.identityaccess.domain.exception.EmailAlreadyUsedException
import com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody.RegisterUserRequestBody
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@RestController
@RequestMapping("/")
class AuthController(
    val userService: UserService
) {

    @GetMapping("login")
    fun loginPage(): ModelAndView = ModelAndView("loginPage")

    @GetMapping("sign-up")
    fun registrationPage(): ModelAndView = ModelAndView("registrationPage", "body", RegisterUserRequestBody())

    @PostMapping("user/register")
    fun registerUser(
        @Valid @ModelAttribute("body") body: RegisterUserRequestBody,
        result: BindingResult
    ): ModelAndView {
        if (result.hasErrors()) return ModelAndView("registrationPage", "body", body)

        if (body.password != body.passwordConfirmation) {
            result.addError(FieldError("body", "passwordConfirmation", "Password confirmation is not matching"))
            return ModelAndView("registrationPage", "body", body)
        }

        try {
            userService.addUser(AddUserCommand(body.email, body.password))
        } catch (e: EmailAlreadyUsedException) {
            result.addError(FieldError("body", "email", e.message))
            return ModelAndView("registrationPage", "body", body)
        }

        return ModelAndView("redirect:/login")
    }
}