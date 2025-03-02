package br.brunorodrigues.authserver.products

import br.brunorodrigues.authserver.users.SortDir
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductService(
    val productRepository: ProductRepository,
) {
    fun insert(product: Product) = productRepository.save(product)
    fun findAll(dir: SortDir): List<Product> {
        return when (dir) {
            SortDir.ASC -> productRepository.findAll(Sort.by("name"))
            SortDir.DESC -> productRepository.findAll(Sort.by("name").descending())
        }
    }

    fun findByIdOrNull(id: Long) = productRepository.findByIdOrNull(id)
    fun delete(id: Long) = productRepository.deleteById(id)
}
