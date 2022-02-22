package com.example.ui.repos;

import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntityDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformDataRequestWrapperEntityRepository extends JpaRepository<PlatformDataRequestWrapperEntityDAO, Long> {
}
