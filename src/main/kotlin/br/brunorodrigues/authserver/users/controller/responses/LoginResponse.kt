package br.brunorodrigues.authserver.users.controller.responses

data class LoginResponse(
    val token: String,
    val user: UserResponse,
)
