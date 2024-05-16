package com.ecommerce.repository;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByEmail(String email);
    Cart findByEmailAndProduct(String email, Product product);
    void deleteByEmail(String email);
    Cart findByIdAndQuantity(long id,int quantity);
    @Transactional
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.email = :email AND c.product = :product")
    void deleteCartBy(@Param("email") String email, @Param("product") Product product);
}
