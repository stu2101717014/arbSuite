package com.example.hs.services;

import com.example.hs.data.NamesSimilaritiesDAO;
import dtos.ResultEntityDTO;

import java.util.List;

public interface NamesSimilaritiesService {

    List<NamesSimilaritiesDAO> getAll();

    List<NamesSimilaritiesDAO> saveAllAndFlushNamesSimilarities(List<NamesSimilaritiesDAO> namesSimilaritiesDAOList);

    List<NamesSimilaritiesDAO> deleteNamesSimilarities(List<NamesSimilaritiesDAO> namesSimilaritiesDAOList);

    void remapNamesSimilarities(List<ResultEntityDTO> resultEntityDAOList, List<NamesSimilaritiesDAO> namesSimilaritiesDAOList);
}

