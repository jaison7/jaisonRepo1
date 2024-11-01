package com.user.nosql.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.user.nosql.entity.OrderMetadata;

public interface OrderMongoRepository extends MongoRepository<OrderMetadata, String> {
	
}
