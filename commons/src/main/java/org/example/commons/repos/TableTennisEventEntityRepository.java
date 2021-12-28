package org.example.commons.repos;

import org.example.commons.entities.TableTennisEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableTennisEventEntityRepository extends JpaRepository<TableTennisEventEntity, Long> {
}