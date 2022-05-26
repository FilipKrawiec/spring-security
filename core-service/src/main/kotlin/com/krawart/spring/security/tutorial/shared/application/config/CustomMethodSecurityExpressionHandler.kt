package com.krawart.spring.security.tutorial.shared.application.config

import org.aopalliance.intercept.MethodInvocation
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations
import org.springframework.security.authentication.AuthenticationTrustResolverImpl
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class CustomMethodSecurityConfiguration : GlobalMethodSecurityConfiguration() {

    override fun createExpressionHandler(): MethodSecurityExpressionHandler {
        return CustomMethodSecurityExpressionHandler()
    }

    inner class CustomMethodSecurityExpressionHandler : DefaultMethodSecurityExpressionHandler() {
        override fun createSecurityExpressionRoot(
            authentication: Authentication,
            invocation: MethodInvocation
        ): MethodSecurityExpressionOperations {
            val root = CustomMethodSecurityExpressionRoot(authentication)
            root.`this` = invocation.`this`
            root.setPermissionEvaluator(permissionEvaluator)
            root.setTrustResolver(AuthenticationTrustResolverImpl())
            root.setRoleHierarchy(roleHierarchy)
            root.setDefaultRolePrefix("ROLE_")

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

        fun setThis(target: Any?) {
            this.target = target
        }

        override fun getThis(): Any? = target

        @Suppress("unused")
        fun isAdmin(): Boolean = (principal as User).authorities.contains(SimpleGrantedAuthority("ADMIN"))
    }
}


