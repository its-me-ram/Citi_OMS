package com.citi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.citi.entity.ExecutedTable;

@Repository
public interface ExecutedDAO extends JpaRepository<ExecutedTable, Integer> {

}
