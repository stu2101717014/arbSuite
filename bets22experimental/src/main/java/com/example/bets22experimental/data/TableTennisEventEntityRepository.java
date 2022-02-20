package com.example.bets22experimental.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableTennisEventEntityRepository extends JpaRepository<TableTennisEventEntity, Long> {
}