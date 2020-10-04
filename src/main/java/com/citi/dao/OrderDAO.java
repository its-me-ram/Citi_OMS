package com.citi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.citi.entity.OrderGenerator;
@Repository
public interface OrderDAO extends JpaRepository<OrderGenerator, Integer> {

}
