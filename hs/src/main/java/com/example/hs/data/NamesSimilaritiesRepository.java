package com.example.hs.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NamesSimilaritiesRepository extends JpaRepository<NamesSimilaritiesDAO, Long> {


}
