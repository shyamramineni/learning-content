package com.widgetario.controllers;

import com.widgetario.models.Product;
import com.widgetario.repositories.ProductRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class ProductsControllerTest {

  @InjectMocks
  private ProductsController controller;
  @Mock
  private ProductRepository repository;
  @Mock
  MeterRegistry registry;
  @Mock
  private Counter counter;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetProducts() {
    // Mock repository response
    List<Product> mockProducts = new ArrayList<>();
    mockProducts.add(new Product("Product 1", new BigDecimal("10.00")));
    mockProducts.add(new Product("Product 2", new BigDecimal("20.00")));
    when(repository.findAll()).thenReturn(mockProducts);
    when(registry.counter(any(), any(), any())).thenReturn(counter);
    doNothing().when(counter).increment();

    ReflectionTestUtils.setField(controller, "priceFactor", "1");
    // Call controller method
    List<Product> result = controller.get();

    // Verify that the products' prices are updated correctly
    assertEquals(new BigDecimal("10"), result.get(0).getPrice());
    assertEquals(new BigDecimal("20"), result.get(1).getPrice());
  }
}