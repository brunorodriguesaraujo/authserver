package br.brunorodrigues.authserver.orders.controller.requests

data class CreateOrderRequest(
    val items: List<OrderItemRequest>
)