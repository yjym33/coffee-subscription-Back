package com.beanbliss.subscription.repository;

import com.beanbliss.subscription.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}


