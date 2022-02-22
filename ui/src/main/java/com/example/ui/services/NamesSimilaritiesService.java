package com.example.ui.services;

import com.example.ui.entities.jpa.NamesSimilaritiesDAO;
import com.example.ui.repos.NamesSimilaritiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NamesSimilaritiesService {

    private NamesSimilaritiesRepository namesSimilaritiesRepository;

    @Autowired
    public NamesSimilaritiesService(NamesSimilaritiesRepository namesSimilaritiesRepository) {
        this.namesSimilaritiesRepository = namesSimilaritiesRepository;
    }

    public List<NamesSimilaritiesDAO> getAll(){
        return namesSimilaritiesRepository.findAll();
    }

    public NamesSimilaritiesDAO saveAndFlushNameSimilarity(NamesSimilaritiesDAO namesSimilaritiesDAO){
        return this.namesSimilaritiesRepository.saveAndFlush(namesSimilaritiesDAO);
    }

    public void deleteNameSimilarity(NamesSimilaritiesDAO namesSimilaritiesDAO){
        namesSimilaritiesRepository.delete(namesSimilaritiesDAO);
    }

}
