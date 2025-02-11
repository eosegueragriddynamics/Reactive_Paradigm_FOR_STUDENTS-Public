package com.example.reactive_paradigm_project_edom.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@Getter
@Setter
public class UserInfo {

    @Id
    private String id;

    @Field("name")
    private String userName;

    @Field("phone")
    private String phoneNumber;

    public UserInfo() {
    }

    public UserInfo(String id, String userName, String phoneNumber) {
        this.id = id;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }
}
