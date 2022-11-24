package com.krawart.spring.security.tutorial.shared.application.config

import com.krawart.spring.security.tutorial.identityaccess.domain.Authority
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class AuthorizationConfiguration(
    private val rememberMeTokenDao: PersistentTokenRepository,
    private val authenticationDetailsSource: CustomWebAuthenticationDetailsSource,
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
            .antMatchers(
                "/sign-up",
                "/users/register",
                "/users/reset-password",
                "/registration-confirmation",
                "/forgot-password"
            ).permitAll()

            .antMatchers("/user/**").hasAuthority(Authority.USER.name)

            .antMatchers("/admin/**").hasAuthority(Authority.ADMIN.name)

            .anyRequest().authenticated()

        http.formLogin().permitAll()
            .loginProcessingUrl("/login/process")
            .loginPage("/login")
            .authenticationDetailsSource(authenticationDetailsSource)
            .and().httpBasic()

            .and().rememberMe()
            .tokenValiditySeconds(604800) // One week in seconds
            .key("lssAppKey") // Secret used to validate rm cookie
            // .useSecureCookie(true) // Only for https connection
            .rememberMeCookieName("sticky-cookie") // Name of cookie in browser
            .rememberMeParameter("remember") // Client parameter name
            .tokenRepository(rememberMeTokenDao) // Uses persisted value to validate

            .and().logout().permitAll()
            .logoutRequestMatcher(AntPathRequestMatcher("/logout/process", "POST"))
    }
}
