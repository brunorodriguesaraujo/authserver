package br.brunorodrigues.authserver.orders.models

import br.brunorodrigues.authserver.products.Product
import jakarta.persistence.*

@Entity
class OrderItem(
    @Id @GeneratedValue
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "idOrder")
    val order: Order,

    @ManyToOne
    @JoinColumn(name = "idProduct")
    val product: Product,

    val quantity: Int
)