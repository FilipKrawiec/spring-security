package com.krawart.spring.security.tutorial.security.application.configuration

import com.krawart.spring.security.tutorial.security.domain.UserRole
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class AuthorizationConfiguration(
    private val passwordEncoder: PasswordEncoder,
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.inMemoryAuthentication()
            .withUser("user")
            .password(passwordEncoder.encode("password"))
            .authorities(UserRole.USER.name)
            .and().withUser("admin")
            .password(passwordEncoder.encode("password"))
            .authorities(UserRole.ADMIN.name)
    }

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
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
