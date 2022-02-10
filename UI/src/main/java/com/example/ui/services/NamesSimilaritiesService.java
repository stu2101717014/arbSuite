package com.example.ui.services;

import com.example.ui.entities.jpa.NamesSimilarities;
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

    public List<NamesSimilarities> getAll(){
        return namesSimilaritiesRepository.findAll();
    }

    public NamesSimilarities saveAndFlushNameSimilarity(NamesSimilarities namesSimilarities){
        return this.namesSimilaritiesRepository.saveAndFlush(namesSimilarities);
    }

    public void deleteNameSimilarity(NamesSimilarities namesSimilarities){
        namesSimilaritiesRepository.delete(namesSimilarities);
    }

}
