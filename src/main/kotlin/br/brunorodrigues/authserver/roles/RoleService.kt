package br.brunorodrigues.authserver.roles

import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RoleService(
    val roleRepository: RoleRepository
) {
    fun save(role: Role) =
        roleRepository.save(role)
    fun findAll() =
        roleRepository.findAll(Sort.by("name"))
    fun findByNameOrNull(name: String) =
        roleRepository.findByIdOrNull(name)
}
