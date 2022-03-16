package com.example.ui.services;

import com.example.ui.entities.jpa.NamesSimilaritiesDAO;
import com.example.ui.entities.jpa.ResultEntityDAO;
import com.example.ui.entities.jpa.TableTennisEventEntityDAO;
import com.example.ui.repos.NamesSimilaritiesRepository;
import com.example.ui.services.interfaces.NamesSimilaritiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class NamesSimilaritiesServiceImpl implements NamesSimilaritiesService {

    private NamesSimilaritiesRepository namesSimilaritiesRepository;

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

    public void remapNamesSimilarities(List<ResultEntityDAO> resultEntityDAOList, List<NamesSimilaritiesDAO> namesSimilaritiesDAOList) {

        HashMap<String, HashMap<String, NamesSimilaritiesDAO>> helperMap = new HashMap<>();

        for (NamesSimilaritiesDAO namesSimilarity : namesSimilaritiesDAOList) {
            if (!helperMap.containsKey(namesSimilarity.getPlatformName())) {
                helperMap.put(namesSimilarity.getPlatformName(), new HashMap<String, NamesSimilaritiesDAO>());
            }
            helperMap.get(namesSimilarity.getPlatformName()).put(namesSimilarity.getPlatformSpecificPlayerName(), namesSimilarity);
        }

        for (ResultEntityDAO resultEntity : resultEntityDAOList) {
            Set<TableTennisEventEntityDAO> tableTennisEventEntitySet = resultEntity.getTableTennisEventEntitySet();
            for (TableTennisEventEntityDAO ttee : tableTennisEventEntitySet) {
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
