package com.example.hs.services;

import com.example.hs.data.NamesSimilaritiesDAO;
import com.example.hs.data.NamesSimilaritiesRepository;
import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class NamesSimilaritiesServiceImpl implements NamesSimilaritiesService {

    private final NamesSimilaritiesRepository namesSimilaritiesRepository;

    @Autowired
    public NamesSimilaritiesServiceImpl(NamesSimilaritiesRepository namesSimilaritiesRepository) {
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

    public void remapNamesSimilarities(List<ResultEntityDTO> resultEntityDAOList, List<NamesSimilaritiesDAO> namesSimilaritiesDAOList) {

        HashMap<String, HashMap<String, NamesSimilaritiesDAO>> helperMap = new HashMap<>();

        for (NamesSimilaritiesDAO namesSimilarity : namesSimilaritiesDAOList) {
            if (!helperMap.containsKey(namesSimilarity.getPlatformName())) {
                helperMap.put(namesSimilarity.getPlatformName(), new HashMap<String, NamesSimilaritiesDAO>());
            }
            helperMap.get(namesSimilarity.getPlatformName()).put(namesSimilarity.getPlatformSpecificPlayerName(), namesSimilarity);
        }

        for (ResultEntityDTO resultEntity : resultEntityDAOList) {
            Set<TableTennisEventEntityDTO> tableTennisEventEntitySet = resultEntity.getTableTennisEventEntitySet();
            for (TableTennisEventEntityDTO ttee : tableTennisEventEntitySet) {
                if (helperMap.containsKey(resultEntity.getPlatformName()) && helperMap.get(resultEntity.getPlatformName()).containsKey(ttee.getFirstPlayerName())) {
                    NamesSimilaritiesDAO forReplace = helperMap.get(resultEntity.getPlatformName()).get(ttee.getFirstPlayerName());
                    ttee.setFirstPlayerName(forReplace.getUniversalPlayerName());
                }
                if (helperMap.containsKey(resultEntity.getPlatformName()) && helperMap.get(resultEntity.getPlatformName()).containsKey(ttee.getSecondPlayerName())) {
                    NamesSimilaritiesDAO forReplace = helperMap.get(resultEntity.getPlatformName()).get(ttee.getSecondPlayerName());
                    ttee.setSecondPlayerName(forReplace.getUniversalPlayerName());
                }
            }
        }
    }
}
