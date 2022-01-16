package org.example.commons.bet365.services;

import org.example.commons.entities.ResultEntity;
import org.example.commons.entities.TableTennisEventEntity;
import org.example.commons.repos.ResultEntityRepository;
import org.example.commons.repos.TableTennisEventEntityRepository;
import org.example.commons.services.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.Optional;

@Service
public class Bet365Service {

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    private HttpService httpService;

    @Autowired
    public Bet365Service(ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository, HttpService httpService) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
        this.httpService = httpService;
    }

    public String createAndExecuteTableTennisUserStory() {
        try {

            extractdataFromBet365Core();

            Long id = resultEntityRepository.findAll().stream().max(Comparator.comparing(ResultEntity::getId)).get().getId();
            ResultEntity byId1 = resultEntityRepository.findById(id).get();

            return httpService.serializeResultEnt(byId1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void extractdataFromBet365Core() throws IOException {
        String path = Bet365Service.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8").substring(1);

        System.out.println(decodedPath);

        Process proc = Runtime.getRuntime().exec("java -jar " + decodedPath + "bet354Core-1.17-SNAPSHOT.jar");
        BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        while (( line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();


        int i = proc.exitValue();
    }
}


