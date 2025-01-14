package kr.hhplus.be.server.infra.product;

import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Product save(Product product);
}
