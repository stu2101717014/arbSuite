package com.example.ui.services.interfaces;



import dtos.NamesSimilaritiesDTO;

import java.util.List;

public interface NamesSimilaritiesService {

    List<NamesSimilaritiesDTO> getAll();

    NamesSimilaritiesDTO saveAndFlushNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDAO);

    List<NamesSimilaritiesDTO> deleteNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDTO);

}
