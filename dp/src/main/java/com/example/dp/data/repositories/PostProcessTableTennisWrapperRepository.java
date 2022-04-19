package com.example.dp.data.repositories;

import com.example.dp.data.entities.PostProcessTableTennisWrapperDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface PostProcessTableTennisWrapperRepository extends JpaRepository<PostProcessTableTennisWrapperDAO, Long> {

    @Query("select pd from PostProcessTableTennisWrapperDAO pd where pd.time = (SELECT MAX(pdi.time) FROM PostProcessTableTennisWrapperDAO pdi)")
    PostProcessTableTennisWrapperDAO getLatestProcessedData();

    @Transactional
    @Modifying
    @Query("delete from PostProcessTableTennisWrapperDAO where time < :date")
    void deleteRecordsCreatedEarlierThan(Date date);
}