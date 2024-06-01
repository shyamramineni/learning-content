package com.widgetario.repositories;

import java.util.List;

import com.widgetario.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();
}
