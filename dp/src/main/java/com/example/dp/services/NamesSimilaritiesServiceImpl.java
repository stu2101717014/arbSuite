package com.example.dp.services;

import com.example.dp.mqtt.names.similarities.NSDeleteSender;
import com.example.dp.mqtt.names.similarities.NSGetAllSender;
import com.example.dp.mqtt.names.similarities.NSSaveSender;
import com.example.dp.services.interfaces.NamesSimilaritiesService;
import dtos.NamesSimilaritiesDTO;
import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class NamesSimilaritiesServiceImpl implements NamesSimilaritiesService {

    private final NSGetAllSender nsGetAllSender;

    private final NSSaveSender nsSaveSender;

    private final NSDeleteSender nsDeleteSender;

    @Autowired
    public NamesSimilaritiesServiceImpl(
            NSGetAllSender nsGetAllSender,
            NSSaveSender nsSaveSender,
            NSDeleteSender nsDeleteSender) {
        this.nsGetAllSender = nsGetAllSender;
        this.nsSaveSender = nsSaveSender;
        this.nsDeleteSender = nsDeleteSender;
    }


    public List<NamesSimilaritiesDTO> getAll() {
        return this.nsGetAllSender.getAllNamesSimilarities();
    }

    public List<NamesSimilaritiesDTO> saveAndFlushNamesSimilarities(List<NamesSimilaritiesDTO> namesSimilaritiesDTOList) {
        return this.nsSaveSender.saveNamesSimilarities(namesSimilaritiesDTOList);
    }

    public List<NamesSimilaritiesDTO> deleteNamesSimilarities(List<NamesSimilaritiesDTO> namesSimilaritiesDTOList) {
        return this.nsDeleteSender.deleteNamesSimilarities(namesSimilaritiesDTOList);
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
