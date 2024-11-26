package com.order.service;

import com.order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public Long getOrderCountByProductId(Long productId) {
        AtomicLong temp = new AtomicLong();
        productRepository.findAllByProductId(productId).forEach(e-> temp.addAndGet(e.getCount()));
        return temp.get();
    }
}
