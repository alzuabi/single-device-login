package com.framework.module.auth.repository

import com.framework.module.auth.entities.Authorities
import com.framework.module.auth.entities.Roles
import com.framework.module.auth.entities.User
import com.framework.module.auth.enums.RoleName
import org.springframework.data.jpa.repository.JpaRepository
//import com.framework.module.base.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsUserByUsername(username: String): Boolean

}

@Repository
interface RoleRepository : JpaRepository<Roles, Long> {
    fun findByRoleName(roleName: RoleName): Roles?
}

@Repository
interface AuthorityRepository : JpaRepository<Authorities, Long> {
}