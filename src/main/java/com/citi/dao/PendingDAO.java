package com.citi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.citi.entity.OrderGenerator;
import com.citi.entity.PendingTable;

@Repository
public interface PendingDAO extends JpaRepository<PendingTable, Integer>{

}
