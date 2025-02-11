package com.example.reactive_paradigm_project_edom.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInfo {
    private String orderNumber;
    private String userName;
    private String phoneNumber;
    private String productCode;

    private String productName;
    private String productId;
}
