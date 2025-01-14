package kr.hhplus.be.server.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ProductRepository {

    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Product save(Product product);
    Optional<Product> findById(Long id);
}
