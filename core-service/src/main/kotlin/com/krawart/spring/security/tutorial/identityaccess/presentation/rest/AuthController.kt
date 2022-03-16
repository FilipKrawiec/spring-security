package com.krawart.spring.security.tutorial.identityaccess.presentation.rest

import com.krawart.spring.security.tutorial.identityaccess.application.UserService
import com.krawart.spring.security.tutorial.identityaccess.application.command.AddUserCommand
import com.krawart.spring.security.tutorial.identityaccess.domain.exception.EmailAlreadyUsedException
import com.krawart.spring.security.tutorial.identityaccess.domain.exception.InvalidPasswordConfirmationException
import com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody.RegisterUserRequestBody
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@Controller
@RequestMapping("/")
class AuthController(
    val userService: UserService
) {

    @GetMapping("login")
    fun loginPage(): String = "loginPage"

    @GetMapping("sign-up")
    fun registrationPage(): ModelAndView = ModelAndView("registrationPage", "user", RegisterUserRequestBody())

    @PostMapping("user/register")
    fun registerUser(@Valid body: RegisterUserRequestBody, result: BindingResult): ModelAndView {
        if (result.hasErrors()) return ModelAndView("registrationPage", "user", body)

        validatePasswordConfirmation(body)

        try {
            userService.addUser(AddUserCommand(body.email, body.password))
        } catch (e: EmailAlreadyUsedException) {
            result.addError(FieldError("user", "email", e.message))
            return ModelAndView("registrationPage", "user", body)
        }

        return ModelAndView("redirect:/login")
    }

    private fun validatePasswordConfirmation(body: RegisterUserRequestBody) {
        if (body.password != body.passwordConfirmation) {
            throw InvalidPasswordConfirmationException("Password confirmation must be valid")
        }
    }
}