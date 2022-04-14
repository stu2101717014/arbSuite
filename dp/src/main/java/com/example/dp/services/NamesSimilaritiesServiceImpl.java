package com.example.dp.services;

import com.example.dp.mqtt.NSSender;
import com.example.dp.services.interfaces.NamesSimilaritiesService;
import dtos.NamesSimilaritiesDTO;
import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class NamesSimilaritiesServiceImpl implements NamesSimilaritiesService {

    private final NSSender nsSender;

    @Autowired
    public NamesSimilaritiesServiceImpl(NSSender nsSender) {
        this.nsSender = nsSender;
    }


    public List<NamesSimilaritiesDTO> getAll() {
        return nsSender.getAllNamesSimilarities();
    }

    public NamesSimilaritiesDTO saveAndFlushNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDAO) {

        throw new NotImplementedException();
    }

    public void deleteNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDAO) {

        throw new NotImplementedException();
    }

    public void remapNamesSimilarities(List<ResultEntityDTO> resultEntityDTODAOList, List<NamesSimilaritiesDTO> namesSimilaritiesDAOList) {

        HashMap<String, HashMap<String, NamesSimilaritiesDTO>> helperMap = new HashMap<>();

        for (NamesSimilaritiesDTO namesSimilarity : namesSimilaritiesDAOList) {
            if (!helperMap.containsKey(namesSimilarity.getPlatformName())) {
                helperMap.put(namesSimilarity.getPlatformName(), new HashMap<String, NamesSimilaritiesDTO>());
            }
            helperMap.get(namesSimilarity.getPlatformName()).put(namesSimilarity.getPlatformSpecificPlayerName(), namesSimilarity);
        }

        for (ResultEntityDTO resultEntityDTO : resultEntityDTODAOList) {
            Set<TableTennisEventEntityDTO> tableTennisEventEntitySet = resultEntityDTO.getTableTennisEventEntitySet();
            for (TableTennisEventEntityDTO ttee : tableTennisEventEntitySet) {
                if (helperMap.containsKey(resultEntityDTO.getPlatformName()) && helperMap.get(resultEntityDTO.getPlatformName()).containsKey(ttee.getFirstPlayerName())) {
                    NamesSimilaritiesDTO forReplace = helperMap.get(resultEntityDTO.getPlatformName()).get(ttee.getFirstPlayerName());
                    ttee.setFirstPlayerName(forReplace.getUniversalPlayerName());
                }
                if (helperMap.containsKey(resultEntityDTO.getPlatformName()) && helperMap.get(resultEntityDTO.getPlatformName()).containsKey(ttee.getSecondPlayerName())) {
                    NamesSimilaritiesDTO forReplace = helperMap.get(resultEntityDTO.getPlatformName()).get(ttee.getSecondPlayerName());
                    ttee.setSecondPlayerName(forReplace.getUniversalPlayerName());
                }
            }
        }
    }

}
