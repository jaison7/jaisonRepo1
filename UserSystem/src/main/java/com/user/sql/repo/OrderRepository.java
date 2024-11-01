package com.user.sql.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.sql.entity.Order;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long> {
	
	public Optional<Order> findById(Long Id);

}
