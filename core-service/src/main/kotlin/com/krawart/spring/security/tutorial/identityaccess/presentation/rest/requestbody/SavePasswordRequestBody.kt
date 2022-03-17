package com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody

import javax.validation.constraints.NotNull

data class SavePasswordRequestBody(
    @NotNull val password: String = "",
    @NotNull val passwordConfirmation: String = "",
)
