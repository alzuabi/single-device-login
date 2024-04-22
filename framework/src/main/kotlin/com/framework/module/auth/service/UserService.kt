package com.framework.module.auth.service

import com.framework.module.auth.entities.User
import com.framework.module.auth.enums.RoleName
import com.framework.module.auth.repository.RoleRepository
import com.framework.module.auth.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authenticationManager: AuthenticationManager,
) {
    fun findByUsername(username: String) = userRepository.findByUsername(username)
    fun saveUser(user: User): User = userRepository.save(user)
    fun getRole(roleName: RoleName = RoleName.ROLE_CUSTOMER) = roleRepository.findByRoleName(roleName)!!
    fun existsUserByUsername(username: String) = userRepository.existsUserByUsername(username)

    fun authenticateUser(username: String, password: String): Authentication =
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))

}


