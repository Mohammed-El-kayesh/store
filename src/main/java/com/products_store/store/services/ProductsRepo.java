package com.products_store.store.services;

import com.products_store.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepo extends JpaRepository<Product,Integer>
{
}
