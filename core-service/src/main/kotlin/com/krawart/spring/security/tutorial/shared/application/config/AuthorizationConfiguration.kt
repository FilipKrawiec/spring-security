package com.krawart.spring.security.tutorial.shared.application.config

import com.krawart.spring.security.tutorial.identityaccess.application.UserService
import com.krawart.spring.security.tutorial.identityaccess.domain.UserRole
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class AuthorizationConfiguration(
    private val userService: UserService,
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
    }

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
            .antMatchers(
                "/sign-up",
                "/users/register",
                "/users/reset-password",
                "/registration-confirmation",
                "/forgot-password"
            ).permitAll()

            .antMatchers("/user/**").hasAuthority(UserRole.USER.name)

            .antMatchers("/admin/**").hasAuthority(UserRole.ADMIN.name)

            .anyRequest().authenticated()

        http.formLogin().permitAll()
            .loginProcessingUrl("/login/process")
            .loginPage("/login")
            .and().httpBasic()
            .and().logout().permitAll()
            .logoutRequestMatcher(AntPathRequestMatcher("/logout/process", "POST"))
    }
}
