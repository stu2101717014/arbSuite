package com.example.ui.repos;

import com.example.ui.entities.jpa.NamesSimilarities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NamesSimilaritiesRepository extends JpaRepository<NamesSimilarities, Long> {

}
