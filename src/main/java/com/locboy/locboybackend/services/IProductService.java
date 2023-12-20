package com.locboy.locboybackend.services;

import com.locboy.locboybackend.dtos.ProductDTO;
import com.locboy.locboybackend.dtos.ProductImageDTO;
import com.locboy.locboybackend.exceptions.DataNotFoundException;
import com.locboy.locboybackend.models.Product;
import com.locboy.locboybackend.models.ProductImage;
import com.locboy.locboybackend.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {

    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(long id);

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    void  deleteProduct(long id);

    boolean  existsByName(String name);

    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO
    ) throws Exception;
}
