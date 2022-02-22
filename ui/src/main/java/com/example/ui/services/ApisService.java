package com.example.ui.services;

import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntityDAO;
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

    public List<PlatformDataRequestWrapperEntityDAO> getAll() {
        return this.platformDataRequestWrapperEntityRepository.findAll();
    }

    public PlatformDataRequestWrapperEntityDAO addNewApi(PlatformDataRequestWrapperEntityDAO platformDataRequestWrapperEntityDAO) {
        return this.platformDataRequestWrapperEntityRepository.saveAndFlush(platformDataRequestWrapperEntityDAO);
    }

    public PlatformDataRequestWrapperEntityDAO edit(PlatformDataRequestWrapperEntityDAO platformDataRequestWrapperEntityDAO) {
        return this.platformDataRequestWrapperEntityRepository.saveAndFlush(platformDataRequestWrapperEntityDAO);
    }

    public void delete(PlatformDataRequestWrapperEntityDAO selected) {
        this.platformDataRequestWrapperEntityRepository.delete(selected);
    }
}
