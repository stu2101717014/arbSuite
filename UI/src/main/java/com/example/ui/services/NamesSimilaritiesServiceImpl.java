package com.example.ui.services;


import com.example.ui.mqtt.NamesSimilaritiesDeleteSender;
import com.example.ui.mqtt.NamesSimilaritiesGetAllSender;
import com.example.ui.mqtt.NamesSimilaritiesSaveAndFlushSender;
import com.example.ui.services.interfaces.NamesSimilaritiesService;
import dtos.NamesSimilaritiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NamesSimilaritiesServiceImpl implements NamesSimilaritiesService {

    private final NamesSimilaritiesGetAllSender namesSimilaritiesGetAllSender;

    private final NamesSimilaritiesSaveAndFlushSender namesSimilaritiesSaveAndFlushSender;

    private final NamesSimilaritiesDeleteSender namesSimilaritiesDeleteSender;

    @Autowired
    public NamesSimilaritiesServiceImpl(NamesSimilaritiesGetAllSender namesSimilaritiesGetAllSender,
                                        NamesSimilaritiesSaveAndFlushSender namesSimilaritiesSaveAndFlushSender,
                                        NamesSimilaritiesDeleteSender namesSimilaritiesDeleteSender) {
        this.namesSimilaritiesGetAllSender = namesSimilaritiesGetAllSender;
        this.namesSimilaritiesSaveAndFlushSender = namesSimilaritiesSaveAndFlushSender;
        this.namesSimilaritiesDeleteSender = namesSimilaritiesDeleteSender;
    }

    public List<NamesSimilaritiesDTO> getAll() {
        List<NamesSimilaritiesDTO> allNamesSimilarities = this.namesSimilaritiesGetAllSender.getAllNamesSimilarities();
        return allNamesSimilarities;
    }

    public NamesSimilaritiesDTO saveAndFlushNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDTO) {
        List<NamesSimilaritiesDTO> namesSimilaritiesDTOList = new ArrayList<>();
        namesSimilaritiesDTOList.add(namesSimilaritiesDTO);
        List<NamesSimilaritiesDTO> resultList = this.namesSimilaritiesSaveAndFlushSender.saveAndFlushNamesSimilarities(namesSimilaritiesDTOList);
        Optional<NamesSimilaritiesDTO> any = resultList.stream().findAny();
        if (any.isPresent()) {
            return any.get();
        }
        return new NamesSimilaritiesDTO();
    }

    public List<NamesSimilaritiesDTO> deleteNameSimilarity(NamesSimilaritiesDTO namesSimilaritiesDTO) {
        List<NamesSimilaritiesDTO> list = new ArrayList<>();
        list.add(namesSimilaritiesDTO);
        return this.namesSimilaritiesDeleteSender.deleteNamesSimilarities(list);
    }

}
