package br.brunorodrigues.authserver.users.controller

import br.brunorodrigues.authserver.security.UserToken
import br.brunorodrigues.authserver.users.SortDir
import br.brunorodrigues.authserver.users.UserService
import br.brunorodrigues.authserver.users.controller.requests.CreateUserRequest
import br.brunorodrigues.authserver.users.controller.requests.LoginRequest
import br.brunorodrigues.authserver.users.controller.responses.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
class UserController(
    val userService: UserService
) {
    @GetMapping("/check")
    fun ping() = "Pong"

    @PostMapping
    fun insert(@RequestBody @Valid user: CreateUserRequest) =
        userService.insert(user.toUser())
            .let { UserResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun findAll(@RequestParam dir: String = "ASC", @RequestParam role: String? = null): ResponseEntity<List<UserResponse>> {
        val sortDir = SortDir.findOrNull(dir) ?: return ResponseEntity.badRequest().build()
        return userService.findAll(sortDir, role)
            .map { UserResponse(it) }
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        userService.findByIdOrNull(id)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun delete(@PathVariable id: String, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        val uid = if (id == "me") user.id else id.toLong()
        return if (user.id == uid || user.isAdmin)
            userService.delete(uid).let { ResponseEntity.ok().build() }
        else
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()

    }

    @PutMapping("/{id}/roles/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "AuthServer")
    fun grant(@PathVariable id: Long, @PathVariable role: String): ResponseEntity<Void> =
        if (userService.addRole(id, role)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()

    @PostMapping("/login")
    fun login(@Valid @RequestBody user: LoginRequest) =
        userService.login(user.email!!, user.password!!)
        ?.let { ResponseEntity.ok().body(it) }
        ?: ResponseEntity.notFound().build()
}
