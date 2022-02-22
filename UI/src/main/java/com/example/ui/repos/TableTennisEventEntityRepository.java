package com.example.ui.repos;

import com.example.ui.entities.jpa.TableTennisEventEntityDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableTennisEventEntityRepository extends JpaRepository<TableTennisEventEntityDAO, Long> {
}
