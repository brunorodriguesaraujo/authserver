package br.brunorodrigues.authserver.orders.models

import br.brunorodrigues.authserver.users.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tblOrders")
class Order(
    @Id @GeneratedValue
    val id: Long? = null,

    val date: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "idUser")
    val user: User,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    val items: MutableList<OrderItem> = mutableListOf()
)