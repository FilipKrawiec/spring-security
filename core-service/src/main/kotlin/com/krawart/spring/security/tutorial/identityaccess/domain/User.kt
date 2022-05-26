package com.krawart.spring.security.tutorial.identityaccess.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id @Column(name = "user_id") val id: UUID,
    @Column(name = "email", nullable = false) val email: String,
    @Column(name = "password", nullable = false) var password: String,
    @Column(name = "is_enabled", nullable = false) var enabled: Boolean,
    @Column(name = "authority", nullable = false) @Enumerated(EnumType.STRING) var authority: Authority,
)