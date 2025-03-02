package br.brunorodrigues.authserver.products.controller

import br.brunorodrigues.authserver.products.Product
import br.brunorodrigues.authserver.products.ProductService
import br.brunorodrigues.authserver.security.UserToken
import br.brunorodrigues.authserver.users.SortDir
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    val productService: ProductService
) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "AuthServer")
    fun insert(@RequestBody @Valid product: Product, auth: Authentication): ResponseEntity<Product>  {
        val user = auth.principal as UserToken
        if (!user.isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        return productService.insert(product)
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @GetMapping
    fun findAll(@RequestParam dir: String = "ASC"): ResponseEntity<List<Product>> {
        val sortDir = SortDir.findOrNull(dir) ?: return ResponseEntity.badRequest().build()
        return productService.findAll(sortDir)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        productService.findByIdOrNull(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "AuthServer")
    fun delete(@PathVariable id: Long, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        if (!user.isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        productService.delete(id)
        return ResponseEntity.ok().build()
    }

}
