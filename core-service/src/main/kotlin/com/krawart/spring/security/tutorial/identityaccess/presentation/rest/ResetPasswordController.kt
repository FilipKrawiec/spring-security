package com.krawart.spring.security.tutorial.identityaccess.presentation.rest

import com.krawart.spring.security.tutorial.identityaccess.application.PasswordResetTokenService
import com.krawart.spring.security.tutorial.identityaccess.application.UserService
import com.krawart.spring.security.tutorial.identityaccess.application.command.CreateResetPasswordTokenCommand
import com.krawart.spring.security.tutorial.identityaccess.application.command.UpdateUserPasswordCommand
import com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody.CreateResetPasswordTokenRequestBody
import com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody.RegisterUserRequestBody
import com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody.ResetPasswordRequestBody
import com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody.SavePasswordRequestBody
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.persistence.EntityNotFoundException
import javax.validation.Valid

@RestController
class ResetPasswordController(
    private val passwordResetTokenService: PasswordResetTokenService,
    private val userService: UserService,
) {

    @GetMapping("/forgot-password")
    fun forgotPasswordPage(@Valid @ModelAttribute("body") body: RegisterUserRequestBody): ModelAndView {
        return ModelAndView("forgotPasswordPage", "body", body)
    }

    @PostMapping("/users/reset-password")
    fun createResetPasswordTokenByEmailPage(
        @Valid @ModelAttribute("body") body: CreateResetPasswordTokenRequestBody,
        result: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        if (result.hasErrors()) return ModelAndView("forgotPasswordPage", "body", body)

        try {
            passwordResetTokenService.create(CreateResetPasswordTokenCommand(body.email))
        } catch (e: EntityNotFoundException) {
            result.addError(FieldError("body", "email", e.message ?: "Email not found"))
            return ModelAndView("forgotPasswordPage", "body", body)
        }
        redirectAttributes.addFlashAttribute("message", "Email with reset token was sent")
        return ModelAndView("redirect:/login")
    }

    @GetMapping("/users/reset-password")
    fun resetPasswordPage(
        @Valid @ModelAttribute("body") body: ResetPasswordRequestBody,
        @RequestParam token: String,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        userService.impersonateUserByResetPasswordTokenValue(token)
        return ModelAndView("resetPasswordPage", "body", body)
    }

    @PostMapping("/users/update-password")
    fun updatePassword(
        @Valid @ModelAttribute("body") body: SavePasswordRequestBody,
        result: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        if (body.password != body.passwordConfirmation) {
            result.addError(FieldError("body", "passwordConfirmation", "Password confirmation does not match"))
            return ModelAndView("resetPasswordPage", "body", body)
        }

        userService.updateCurrentUserPassword(UpdateUserPasswordCommand(body.password))

        redirectAttributes.addFlashAttribute("message", "Password reset successfully")
        return ModelAndView("redirect:/login")
    }
}