package com.example.ui.repos;

import com.example.ui.entities.jpa.TableTennisEventEntityDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface TableTennisEventEntityRepository extends JpaRepository<TableTennisEventEntityDAO, Long> {

}
