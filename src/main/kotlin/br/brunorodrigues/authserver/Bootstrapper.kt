package br.brunorodrigues.authserver

import br.brunorodrigues.authserver.roles.Role
import br.brunorodrigues.authserver.users.User
import br.brunorodrigues.authserver.users.UserRepository
import br.brunorodrigues.authserver.roles.RoleRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    @Value("\${security.admin.email}") private val email: String,
    @Value("\${security.admin.password}") private val password: String,
    @Value("\${security.admin.name}") private val name: String
): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole =
            roleRepository.findByIdOrNull("ADMIN")
                ?: roleRepository.save(Role("ADMIN", "System Administrator"))
                    .also { roleRepository.save(Role("USER", "Premium User")) }

        if (userRepository.findByRole("ADMIN").isEmpty()) {
            val admin = User(
                email= email,
                password = password,
                name= name
            )
            admin.roles.add(adminRole)
            userRepository.save(admin)
        }
    }

}
