package com.example.ui.services;

import com.example.ui.mqtt.GetPlatformNamesSender;
import com.example.ui.mqtt.PPTTWGetLatestSender;
import com.example.ui.services.interfaces.TableTennisService;
import dtos.PostProcessTableTennisWrapperDTO;
import dtos.TableTennisEventWrapperDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Service
public class TableTennisServiceImpl implements TableTennisService {

    private final PPTTWGetLatestSender ppttwGetLatestSender;

    private final GetPlatformNamesSender getPlatformNamesSender;

    @Autowired
    public TableTennisServiceImpl(PPTTWGetLatestSender ppttwGetLatestSender,
                                  GetPlatformNamesSender getPlatformNamesSender) {
        this.ppttwGetLatestSender = ppttwGetLatestSender;
        this.getPlatformNamesSender = getPlatformNamesSender;
    }

    @Override
    public PostProcessTableTennisWrapperDTO getProcessedData() {
        return this.ppttwGetLatestSender.getLatestPostProcessTableTennisWrapper();
    }

    @Override
    public List<String> getPlatformNames() {
        return this.getPlatformNamesSender.getPlatformNames();
    }

    @Override
    public List<TableTennisEventWrapperDTO> sortReshapedData(List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOList) {
        return tableTennisEventWrapperDTOList.stream().sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getEventDate(), nullsLast(naturalOrder())))).sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getSecondPlayer(), nullsLast(naturalOrder())))).sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getFirstPlayer(), nullsLast(naturalOrder())))).collect(Collectors.toList());
    }
}


