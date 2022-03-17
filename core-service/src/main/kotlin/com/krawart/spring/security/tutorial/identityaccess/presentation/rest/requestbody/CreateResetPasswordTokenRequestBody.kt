package com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody

import javax.validation.constraints.NotNull

data class CreateResetPasswordTokenRequestBody(
    @NotNull val email: String = "",
)
