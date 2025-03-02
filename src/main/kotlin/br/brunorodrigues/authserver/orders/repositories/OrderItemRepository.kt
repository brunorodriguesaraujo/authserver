package br.brunorodrigues.authserver.orders.repositories

import br.brunorodrigues.authserver.orders.models.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, Long>