package com.ecommerce.repository;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByEmail(String email);
    Cart findByEmailAndProduct(String email, Product product);
    void deleteByEmail(String email);
    Cart findByIdAndQuantity(long id,int quantity);
}
