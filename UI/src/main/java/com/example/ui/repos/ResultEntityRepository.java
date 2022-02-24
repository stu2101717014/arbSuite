package com.example.ui.repos;

import com.example.ui.entities.jpa.ResultEntityDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ResultEntityRepository extends JpaRepository<ResultEntityDAO, Long> {

    @Query("select distinct re.platformName from ResultEntityDAO re")
    List<String> getAllPlatformNames();

    @Query("select re from ResultEntityDAO re where re.time = (SELECT MAX(rei.time) FROM ResultEntityDAO rei where rei.platformName = :platformName) and re.platformName = :platformName")
    ResultEntityDAO getLatestByPlatformName(String platformName);

    @Query("select t from ResultEntityDAO t where t.time < :date")
    List<ResultEntityDAO> selectAllCreatedBefore(Date date);
}
