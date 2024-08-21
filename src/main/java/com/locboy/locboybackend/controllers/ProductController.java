package com.locboy.locboybackend.controllers;


import com.locboy.locboybackend.dtos.ProductDTO;
import com.locboy.locboybackend.dtos.ProductImageDTO;
import com.locboy.locboybackend.models.Product;
import com.locboy.locboybackend.models.ProductImage;
import com.locboy.locboybackend.responses.ProductListResponse;
import com.locboy.locboybackend.responses.ProductResponse;
import com.locboy.locboybackend.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.locboy.locboybackend.models.ProductImage.MAXIMUM_IMAGES_PRODUCT;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > MAXIMUM_IMAGES_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only uploads 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                //check filesize
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("file is to large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                String fileName = storeFile(file);

                //Save image on ProductImage
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String storeFile(MultipartFile file) throws IOException {

        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        //Add UUID check name file is unique
        String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;

        //PathStore
        java.nio.file.Path uploadDir = Paths.get("uploads");

        //check and create new path if not existing
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);

        //Copy file in main pathStore
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.
                of(page, limit, Sort.by("createAt").descending());

        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);

        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(
            @PathVariable("id") String productId) {
        return ResponseEntity.ok("Product with ID: " + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable long id) {
        return ResponseEntity.ok(String.format("Product with od = % deleted successfully", id));
    }
}
