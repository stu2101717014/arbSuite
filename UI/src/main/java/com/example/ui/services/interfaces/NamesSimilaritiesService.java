package com.example.ui.services.interfaces;

import com.example.ui.entities.jpa.NamesSimilaritiesDAO;
import com.example.ui.entities.jpa.ResultEntityDAO;

import java.util.List;

public interface NamesSimilaritiesService {

    List<NamesSimilaritiesDAO> getAll();

    NamesSimilaritiesDAO saveAndFlushNameSimilarity(NamesSimilaritiesDAO namesSimilaritiesDAO);

    void deleteNameSimilarity(NamesSimilaritiesDAO namesSimilaritiesDAO);

    void remapNamesSimilarities(List<ResultEntityDAO> resultEntityDAOList, List<NamesSimilaritiesDAO> namesSimilaritiesDAOList);
}
