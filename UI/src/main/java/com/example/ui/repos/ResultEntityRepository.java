package com.example.ui.repos;

import com.example.ui.entities.jpa.ResultEntityDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultEntityRepository extends JpaRepository<ResultEntityDAO, Long> {
}
