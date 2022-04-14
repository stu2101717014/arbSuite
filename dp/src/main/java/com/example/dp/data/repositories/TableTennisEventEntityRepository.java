package com.example.dp.data.repositories;

import com.example.dp.data.entities.ResultEntityDAO;
import com.example.dp.data.entities.TableTennisEventEntityDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TableTennisEventEntityRepository extends JpaRepository<TableTennisEventEntityDAO, Long> {

    @Query("select distinct re.platformName from ResultEntityDAO re")
    List<String> getAllPlatformNames();

    @Query("select re from ResultEntityDAO re where re.time = (SELECT MAX(rei.time) FROM ResultEntityDAO rei where rei.platformName = :platformName) and re.platformName = :platformName")
    ResultEntityDAO getLatestByPlatformName(String platformName);

    @Query("select t from ResultEntityDAO t where t.time < :date")
    List<ResultEntityDAO> selectAllCreatedBefore(Date date);

}
