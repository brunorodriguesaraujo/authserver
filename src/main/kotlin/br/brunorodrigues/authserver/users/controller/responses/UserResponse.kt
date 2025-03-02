package br.brunorodrigues.authserver.users.controller.responses

import br.brunorodrigues.authserver.users.User

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
) {
    constructor(user: User): this(id=user.id!!, user.name, user.email)
}
