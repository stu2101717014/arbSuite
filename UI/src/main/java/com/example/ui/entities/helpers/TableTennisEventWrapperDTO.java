package com.example.ui.entities.helpers;

import com.example.ui.entities.jpa.TableTennisEventEntityDAO;

import java.util.Map;

public class TableTennisEventWrapperDTO {

    private TableTennisEventEntityShortDTO tableTennisEventEntityShortDTO;

    private Map<String, TableTennisEventEntityDAO> eventEntityMap;

    private Double arbitragePercentage;

    private String winningPlatformOne;

    private String winningPlatformTwo;

    public TableTennisEventEntityShortDTO getTableTennisEventEntityShort() {
        return tableTennisEventEntityShortDTO;
    }

    public void setTableTennisEventEntityShort(TableTennisEventEntityShortDTO tableTennisEventEntityShortDTO) {
        this.tableTennisEventEntityShortDTO = tableTennisEventEntityShortDTO;
    }

    public Map<String, TableTennisEventEntityDAO> getEventEntityMap() {
        return eventEntityMap;
    }

    public void setEventEntityMap(Map<String, TableTennisEventEntityDAO> eventEntityMap) {
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
