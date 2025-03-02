package br.brunorodrigues.authserver.orders.controller

import br.brunorodrigues.authserver.orders.services.OrderService
import br.brunorodrigues.authserver.orders.controller.requests.CreateOrderRequest
import br.brunorodrigues.authserver.orders.controller.responses.OrderResponse
import br.brunorodrigues.authserver.security.UserToken
import br.brunorodrigues.authserver.users.SortDir
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "AuthServer")
    fun createOrder(@RequestBody request: CreateOrderRequest, auth: Authentication): ResponseEntity<OrderResponse> {
        val user = auth.principal as UserToken
        val userId = user.id
        val order = orderService.createOrder(userId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse(order))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "AuthServer")
    fun delete(@PathVariable id: Long, auth: Authentication): ResponseEntity<Void>{
        val user = auth.principal as UserToken
        val order = orderService.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()

        return if (user.id == order.user.id || user.isAdmin)
            orderService.delete(id).let { ResponseEntity.ok().build() }
        else
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @GetMapping
    fun findAll(@RequestParam dir: String = "ASC"): ResponseEntity<List<OrderResponse>> {
        val sortDir = SortDir.findOrNull(dir) ?: return ResponseEntity.badRequest().build()
        return orderService.findAll(sortDir)
            .map { OrderResponse(it) }
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        orderService.findByIdOrNull(id)
            ?.let { OrderResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}