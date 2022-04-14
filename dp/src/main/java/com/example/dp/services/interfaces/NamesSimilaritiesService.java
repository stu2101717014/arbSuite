package com.example.dp.services.interfaces;

import dtos.NamesSimilaritiesDTO;
import dtos.ResultEntityDTO;

import java.util.List;

public interface NamesSimilaritiesService {

    List<NamesSimilaritiesDTO> getAll();

    NamesSimilaritiesDTO saveAndFlushNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDAO);

    void deleteNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDAO);

    void remapNamesSimilarities(List<ResultEntityDTO> resultEntityDTODAOList, List<NamesSimilaritiesDTO> namesSimilaritiesDAOList);
}