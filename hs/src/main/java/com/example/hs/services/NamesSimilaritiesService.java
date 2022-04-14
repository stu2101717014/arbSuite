package com.example.hs.services;

import com.example.hs.data.NamesSimilaritiesDAO;
import dtos.ResultEntityDTO;

import java.util.List;

public interface NamesSimilaritiesService {

    List<NamesSimilaritiesDAO> getAll();

    NamesSimilaritiesDAO saveAndFlushNameSimilarity(NamesSimilaritiesDAO namesSimilaritiesDAO);

    void deleteNameSimilarity(NamesSimilaritiesDAO namesSimilaritiesDAO);

    void remapNamesSimilarities(List<ResultEntityDTO> resultEntityDAOList, List<NamesSimilaritiesDAO> namesSimilaritiesDAOList);
}

