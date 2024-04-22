package com.framework.module.auth.entities

//import com.framework.module.base.BaseEntity
import com.framework.module.auth.enums.DeviceOS
import com.framework.module.auth.enums.RoleName
import com.framework.module.base.BaseEntity
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.*


@Entity
@Table(name = "customers")
class User(
    private val username: String,
    private val password: String,
    private val accountNonExpired: Boolean = true,
    private val accountNonLocked: Boolean = true,
    private val credentialsNonExpired: Boolean = true,
    private val enabled: Boolean = true,
    @ManyToMany(fetch = FetchType.EAGER) val roles: Set<Roles>
) : BaseEntity<Long>(), UserDetails {


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.flatMap { roles ->
            roles.rolesAuthorities.map { rolesAuthorities ->
                SimpleGrantedAuthority(rolesAuthorities.authorities.authority)
            }
        }.toMutableList()
    }

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = accountNonExpired

    override fun isAccountNonLocked() = accountNonLocked

    override fun isCredentialsNonExpired() = credentialsNonExpired

    override fun isEnabled() = enabled


    override fun equals(other: Any?): Boolean {
        if (other == this) return true
        if (other == null || javaClass != other.javaClass) return false
        return (other as User).username == this.username
    }

    override fun hashCode(): Int {
        return Objects.hash(username)
    }
}

@Entity
class Roles(
    @Enumerated(EnumType.STRING) val roleName: RoleName,
    @OneToMany(mappedBy = "roles", fetch = FetchType.EAGER) val rolesAuthorities: Set<RolesAuthorities>
) : BaseEntity<Long>()

@Embeddable
class RoleAuthorityKey(var roleId: Long, val authorityId: Long) : Serializable

@Entity
class RolesAuthorities(
    @EmbeddedId val roleAuthorityKey: RoleAuthorityKey,
    @ManyToOne @MapsId("roleId") @JoinColumn(name = "role_id") val roles: Roles,
    @ManyToOne @MapsId("authorityId") @JoinColumn(name = "authority_id") val authorities: Authorities,
    val note: String

)

@Entity
class Authorities(
    private val authorityName: String,
    @OneToMany(mappedBy = "authorities") val rolesAuthorities: Set<RolesAuthorities>
) : GrantedAuthority, BaseEntity<Long>() {
    override fun getAuthority() = authorityName
}


@Entity
class Device(
    val deviceUid: UUID,
    @Enumerated(EnumType.STRING) val deviceOs: DeviceOS
) : BaseEntity<Long>() {
    override fun equals(other: Any?): Boolean {
        if (other == this) return true
        if (other == null || javaClass != other.javaClass) return false
        return (other as Device).deviceUid == this.deviceUid
    }

    override fun hashCode(): Int {
        return Objects.hash(deviceUid)
    }
}


@Entity
class ActiveUserDevice(
    @ManyToOne val user: User,
    @OneToOne val device: Device
) : BaseEntity<Long>()