package com.femcoders.electronify.category.dto;

import com.femcoders.electronify.product.dto.ProductResponse;

import java.util.List;

public record CategoryWithProductsResponse(Long id, String name, List<ProductResponse> products) {
}
