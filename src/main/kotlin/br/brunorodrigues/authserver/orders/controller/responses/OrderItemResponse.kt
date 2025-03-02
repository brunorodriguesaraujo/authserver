package br.brunorodrigues.authserver.orders.controller.responses

import br.brunorodrigues.authserver.orders.models.OrderItem

data class OrderItemResponse(
    val productId: Long,
    val name: String,
    val quantity: Int,
    val price: Double
) {
    constructor(orderItem: OrderItem) : this(
        orderItem.product.id!!,
        orderItem.product.name,
        orderItem.quantity,
        orderItem.product.price
    )
}