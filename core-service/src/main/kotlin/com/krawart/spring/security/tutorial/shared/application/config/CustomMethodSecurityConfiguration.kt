package com.krawart.spring.security.tutorial.shared.application.config

import org.aopalliance.intercept.MethodInvocation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class CustomMethodSecurityConfiguration {

    @Bean
    fun createExpressionHandler(): MethodSecurityExpressionHandler = object : DefaultMethodSecurityExpressionHandler() {
        override fun createSecurityExpressionRoot(
            authentication: Authentication,
            invocation: MethodInvocation
        ): MethodSecurityExpressionOperations {
            val root = CustomMethodSecurityExpressionRoot(authentication)
            root.setPermissionEvaluator(permissionEvaluator)
            root.setTrustResolver(trustResolver)
            root.setRoleHierarchy(roleHierarchy)
            root.setDefaultRolePrefix(defaultRolePrefix)
            return root
        }
    }

    inner class CustomMethodSecurityExpressionRoot(authentication: Authentication) :
        SecurityExpressionRoot(authentication),
        MethodSecurityExpressionOperations {

        private var filterObject: Any? = null
        private var returnObject: Any? = null
        private var target: Any? = null

        override fun setFilterObject(filterObject: Any?) {
            this.filterObject = filterObject
        }

        override fun getFilterObject(): Any? = filterObject

        override fun setReturnObject(returnObject: Any?) {
            this.returnObject = returnObject
        }

        override fun getReturnObject(): Any? = returnObject

        override fun getThis(): Any? = target

        @Suppress("unused")
        fun isAdmin(): Boolean = (principal as Authentication).authorities.contains(SimpleGrantedAuthority("ADMIN"))
    }
}


