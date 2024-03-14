package ibm.elizapimentel.DummyProducts.repositories;

import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsRequest, Long> {
    @Query("SELECT p FROM ProductsRequest p WHERE p.category LIKE %?1%")
    List<ProductsRequest> searchCategory(String valor);

    Optional<ProductsRequest> findByTitle(String title);

    @Modifying
    @Transactional
    @Query("UPDATE ProductsRequest p SET p.stock = :newStock WHERE p.id = :id")
    void updateTotalStock(@Param("id") Long id, int newStock);
}
