package com.example.ui.services;

import com.example.ui.mqtt.GetMetricsSender;
import com.example.ui.mqtt.GetPlatformNamesSender;
import com.example.ui.mqtt.PPTTWGetLatestSender;
import com.example.ui.services.interfaces.TableTennisService;
import dtos.MetricsDTO;
import dtos.PostProcessTableTennisWrapperDTO;
import dtos.TableTennisEventWrapperDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Service
public class TableTennisServiceImpl implements TableTennisService {

    private final PPTTWGetLatestSender ppttwGetLatestSender;

    private final GetPlatformNamesSender getPlatformNamesSender;

    private final GetMetricsSender getMetricsSender;

    @Autowired
    public TableTennisServiceImpl(PPTTWGetLatestSender ppttwGetLatestSender,
                                  GetPlatformNamesSender getPlatformNamesSender,
                                  GetMetricsSender getMetricsSender) {
        this.ppttwGetLatestSender = ppttwGetLatestSender;
        this.getPlatformNamesSender = getPlatformNamesSender;
        this.getMetricsSender = getMetricsSender;
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

    @Override
    public MetricsDTO getMetrics() {
        return this.getMetricsSender.getMetrics();
    }
}


