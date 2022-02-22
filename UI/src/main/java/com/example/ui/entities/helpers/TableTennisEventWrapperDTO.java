package com.example.ui.entities.helpers;

import java.util.Map;

public class TableTennisEventWrapperDTO {

    private TableTennisEventEntityShortDTO tableTennisEventEntityShortDTO;

    private Map<String, TableTennisEventEntity> eventEntityMap;

    private Double arbitragePercentage;

    private String winningPlatformOne;

    private String winningPlatformTwo;

    public TableTennisEventEntityShortDTO getTableTennisEventEntityShort() {
        return tableTennisEventEntityShortDTO;
    }

    public void setTableTennisEventEntityShort(TableTennisEventEntityShortDTO tableTennisEventEntityShortDTO) {
        this.tableTennisEventEntityShortDTO = tableTennisEventEntityShortDTO;
    }

    public Map<String, TableTennisEventEntity> getEventEntityMap() {
        return eventEntityMap;
    }

    public void setEventEntityMap(Map<String, TableTennisEventEntity> eventEntityMap) {
        this.eventEntityMap = eventEntityMap;
    }

    public Double getArbitragePercentage() {
        return arbitragePercentage;
    }

    public void setArbitragePercentage(Double arbitragePercentage) {
        this.arbitragePercentage = arbitragePercentage;
    }

    public String getWinningPlatformOne() {
        return winningPlatformOne;
    }

    public void setWinningPlatformOne(String winningPlatformOne) {
        this.winningPlatformOne = winningPlatformOne;
    }

    public String getWinningPlatformTwo() {
        return winningPlatformTwo;
    }

    public void setWinningPlatformTwo(String winningPlatformTwo) {
        this.winningPlatformTwo = winningPlatformTwo;
    }
}
