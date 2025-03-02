package br.brunorodrigues.authserver.orders.controller.requests

data class OrderItemRequest(
    val productId: Long,
    val quantity: Int
)