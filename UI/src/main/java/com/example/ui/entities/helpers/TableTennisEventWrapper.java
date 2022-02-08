package com.example.ui.entities.helpers;

import java.util.Map;


public class TableTennisEventWrapper {

    private TableTennisEventEntityShort tableTennisEventEntityShort;

    private Map<String, TableTennisEventEntity> eventEntityMap;

    private boolean hasArbitrage;

    public TableTennisEventEntityShort getTableTennisEventEntityShort() {
        return tableTennisEventEntityShort;
    }

    public void setTableTennisEventEntityShort(TableTennisEventEntityShort tableTennisEventEntityShort) {
        this.tableTennisEventEntityShort = tableTennisEventEntityShort;
    }

    public Map<String, TableTennisEventEntity> getEventEntityMap() {
        return eventEntityMap;
    }

    public void setEventEntityMap(Map<String, TableTennisEventEntity> eventEntityMap) {
        this.eventEntityMap = eventEntityMap;
    }

    public boolean isHasArbitrage() {
        return hasArbitrage;
    }

    public void setHasArbitrage(boolean hasArbitrage) {
        this.hasArbitrage = hasArbitrage;
    }
}
