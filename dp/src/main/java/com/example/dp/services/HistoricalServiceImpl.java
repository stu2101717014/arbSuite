package com.example.dp.services;

import com.example.dp.mqtt.historical.HistoricalRecordTTEEGetAllSender;
import com.example.dp.mqtt.historical.HistoricalRecordTTEESaveSender;
import com.example.dp.services.helpers.GsonService;
import com.example.dp.services.interfaces.HistoricalService;
import com.google.gson.Gson;
import dtos.HistoricalTableTennisEventWrapperDTO;
import dtos.TableTennisEventWrapperDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HistoricalServiceImpl implements HistoricalService {

    private final GsonService gsonService;

    private final HistoricalRecordTTEESaveSender historicalRecordTTEESaveSender;

    private final HistoricalRecordTTEEGetAllSender historicalRecordTTEEGetAllSender;

    @Autowired
    public HistoricalServiceImpl(GsonService gsonService, HistoricalRecordTTEESaveSender historicalRecordTTEESaveSender,
                                 HistoricalRecordTTEEGetAllSender historicalRecordTTEEGetAllSender) {
        this.gsonService = gsonService;
        this.historicalRecordTTEESaveSender = historicalRecordTTEESaveSender;
        this.historicalRecordTTEEGetAllSender = historicalRecordTTEEGetAllSender;
    }

    @Override
    public List<HistoricalTableTennisEventWrapperDTO> getAllHistoricalRecords() {
        return this.historicalRecordTTEEGetAllSender.getAllHistoricalRecords();
    }

    @Override
    public void persistPositiveArbitrageRecords(List<TableTennisEventWrapperDTO> eventWrapperList) {

        Gson gson = gsonService.getGson();
        List<HistoricalTableTennisEventWrapperDTO> forPersist = new ArrayList<>();
        try {
            for (TableTennisEventWrapperDTO eventWrapperDTO : eventWrapperList) {

                if (eventWrapperDTO.getArbitragePercentage() != null && eventWrapperDTO.getArbitragePercentage() < 1d) {
                    String currentEventWrapperAsJsonString = gson.toJson(eventWrapperDTO);
                    Date detected = new Date(System.currentTimeMillis());

                    HistoricalTableTennisEventWrapperDTO historicalTableTennisEventWrapperDAO = new HistoricalTableTennisEventWrapperDTO();
                    historicalTableTennisEventWrapperDAO.setTime(detected);
                    historicalTableTennisEventWrapperDAO.setHistoricalRecordEventWrapperAsJson(currentEventWrapperAsJsonString);

                    forPersist.add(historicalTableTennisEventWrapperDAO);
                }
            }
            if (forPersist.size() > 0) {
                this.historicalRecordTTEESaveSender.sendHistoricalRecords(forPersist);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
