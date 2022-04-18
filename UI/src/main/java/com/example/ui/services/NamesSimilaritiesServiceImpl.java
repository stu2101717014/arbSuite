package com.example.ui.services;


import com.example.ui.services.interfaces.NamesSimilaritiesService;
import dtos.NamesSimilaritiesDTO;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class NamesSimilaritiesServiceImpl implements NamesSimilaritiesService {


    public List<NamesSimilaritiesDTO> getAll() {
        throw new NotImplementedException();
    }

    public NamesSimilaritiesDTO saveAndFlushNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDTO) {
        throw new NotImplementedException();
    }

    public void deleteNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDTO) {
        throw new NotImplementedException();
    }

}
