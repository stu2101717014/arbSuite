package com.example.ui.repos;

import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformDataRequestWrapperEntityRepository extends JpaRepository<PlatformDataRequestWrapperEntity, Long> {
}
