package br.brunorodrigues.authserver.products

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "tblProducts")
class Product (
    @Id
    val id: Long? = null,

    @NotBlank
    val name: String,
    val price: Double
)
