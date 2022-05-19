package com.example.dp.data.repositories;

import com.example.dp.data.entities.MetricsDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricsRepository extends JpaRepository<MetricsDAO, Long> {
}
