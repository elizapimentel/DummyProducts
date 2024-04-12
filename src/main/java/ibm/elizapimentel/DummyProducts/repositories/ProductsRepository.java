package ibm.elizapimentel.DummyProducts.repositories;

import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsRequest, Long> {
    @Query("SELECT p FROM ProductsRequest p WHERE p.category LIKE %?1%")
    List<ProductsRequest> searchCategory(String valor);

    @Query("SELECT p FROM ProductsRequest p WHERE p.title = :title")
    Optional<ProductsRequest> findByTitle(String title);
}
