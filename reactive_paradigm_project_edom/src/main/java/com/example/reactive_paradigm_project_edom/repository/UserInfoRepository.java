package com.example.reactive_paradigm_project_edom.repository;

import com.example.reactive_paradigm_project_edom.domain.UserInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserInfoRepository extends ReactiveMongoRepository<UserInfo, String> {
    Mono<UserInfo> findById(String userId);
}
