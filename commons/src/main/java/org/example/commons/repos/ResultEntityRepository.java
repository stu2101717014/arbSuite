package org.example.commons.repos;

import org.example.commons.entities.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultEntityRepository extends JpaRepository<ResultEntity, Long> {

}
