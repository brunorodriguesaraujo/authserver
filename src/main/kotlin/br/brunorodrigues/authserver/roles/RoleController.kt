package br.brunorodrigues.authserver.roles

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/roles")
@Tag(name = "Roles")
class RoleController(
    val roleService: RoleService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "AuthServer")
    fun insert(@RequestBody role: Role) =
        roleService.save(role).let {
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(it)
        }

    @GetMapping
    fun findAll() = roleService.findAll()
}
