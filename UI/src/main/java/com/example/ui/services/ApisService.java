package com.example.ui.services;

import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntity;
import com.example.ui.repos.PlatformDataRequestWrapperEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApisService {

    private PlatformDataRequestWrapperEntityRepository platformDataRequestWrapperEntityRepository;

    @Autowired
    public ApisService(PlatformDataRequestWrapperEntityRepository platformDataRequestWrapperEntityRepository) {
        this.platformDataRequestWrapperEntityRepository = platformDataRequestWrapperEntityRepository;
    }

    public List<PlatformDataRequestWrapperEntity> getAll() {
        return this.platformDataRequestWrapperEntityRepository.findAll();
    }

    public PlatformDataRequestWrapperEntity addNewApi(PlatformDataRequestWrapperEntity platformDataRequestWrapperEntity) {
        return this.platformDataRequestWrapperEntityRepository.saveAndFlush(platformDataRequestWrapperEntity);
    }

    public PlatformDataRequestWrapperEntity edit(PlatformDataRequestWrapperEntity platformDataRequestWrapperEntity) {
        return this.platformDataRequestWrapperEntityRepository.saveAndFlush(platformDataRequestWrapperEntity);
    }

    public void delete(PlatformDataRequestWrapperEntity selected) {
        this.platformDataRequestWrapperEntityRepository.delete(selected);
    }
}
