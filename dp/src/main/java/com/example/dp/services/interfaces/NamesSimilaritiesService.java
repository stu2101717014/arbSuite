package com.example.dp.services.interfaces;

import dtos.NamesSimilaritiesDTO;
import dtos.ResultEntityDTO;

import java.util.List;

public interface NamesSimilaritiesService {

    List<NamesSimilaritiesDTO> getAll();

    List<NamesSimilaritiesDTO> saveAndFlushNamesSimilarities(List<NamesSimilaritiesDTO> namesSimilaritiesDTOList);

    List<NamesSimilaritiesDTO> deleteNamesSimilarities(List<NamesSimilaritiesDTO> namesSimilaritiesDTOList);

    void remapNamesSimilarities(List<ResultEntityDTO> resultEntityDTODAOList, List<NamesSimilaritiesDTO> namesSimilaritiesDAOList);
}