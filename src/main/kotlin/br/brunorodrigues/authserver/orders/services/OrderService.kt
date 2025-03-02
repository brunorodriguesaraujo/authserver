package br.brunorodrigues.authserver.orders.services

import br.brunorodrigues.authserver.exception.NotFoundException
import br.brunorodrigues.authserver.orders.controller.requests.CreateOrderRequest
import br.brunorodrigues.authserver.orders.models.Order
import br.brunorodrigues.authserver.orders.models.OrderItem
import br.brunorodrigues.authserver.orders.repositories.OrderRepository
import br.brunorodrigues.authserver.products.ProductRepository
import br.brunorodrigues.authserver.users.SortDir
import br.brunorodrigues.authserver.users.UserRepository
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderService(
    val orderRepository: OrderRepository,
    val productRepository: ProductRepository,
    val userRepository: UserRepository
) {
    fun createOrder(userId: Long, request: CreateOrderRequest): Order {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException("User not found")
        val order = Order(user = user)
        request.items.forEach { itemRequest ->
            val product = productRepository.findById(itemRequest.productId)
                .orElseThrow { NotFoundException("Product not found") }

            val orderItem = OrderItem(order = order, product = product, quantity = itemRequest.quantity)
            order.items.add(orderItem)
        }
        return orderRepository.save(order)
    }
    fun findAll(dir: SortDir): List<Order> {
        return when (dir) {
            SortDir.ASC -> orderRepository.findAll(Sort.by("user"))
            SortDir.DESC -> orderRepository.findAll(Sort.by("user").descending())
        }
    }
    fun delete(id: Long) = orderRepository.deleteById(id)
    fun findByIdOrNull(id: Long): Order? = orderRepository.findByIdOrNull(id)
}

