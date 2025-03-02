package br.brunorodrigues.authserver.orders.controller.responses

import br.brunorodrigues.authserver.orders.models.Order
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val date: LocalDateTime,
    val total: Double,
    val items: List<OrderItemResponse>
) {
    constructor(order: Order) : this(
        id = order.id!!,
        date = order.date,
        total = order.items.sumOf { it.quantity * it.product.price },
        items = order.items.map { OrderItemResponse(it) }
    )
}
