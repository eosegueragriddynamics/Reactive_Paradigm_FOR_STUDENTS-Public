package com.example.reactive_paradigm_project_edom.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInfo {
    private String productId;
    private String productName;
    private Double score;

    public ProductInfo() {
    }

    public ProductInfo(String productId, String productName, Double score) {
        this.productId = productId;
        this.productName = productName;
        this.score = score;
    }
}
