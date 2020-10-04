package com.citi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.citi.entity.RejectedTable;

@Repository
public interface RejectedDAO extends JpaRepository<RejectedTable, Integer> {

}
