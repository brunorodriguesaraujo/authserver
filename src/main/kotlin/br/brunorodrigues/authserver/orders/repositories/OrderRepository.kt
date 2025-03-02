package br.brunorodrigues.authserver.orders.repositories

import br.brunorodrigues.authserver.orders.models.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>