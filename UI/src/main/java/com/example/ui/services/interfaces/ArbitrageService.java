package com.example.ui.services.interfaces;

import com.example.ui.entities.helpers.TableTennisEventWrapperDTO;

import java.util.List;

public interface ArbitrageService {

    List<Double> calculateBets(double investment, List<Double> odds, double arbitragePercentage);

    void checkForArbitrage(List<TableTennisEventWrapperDTO> eventWrapperList);

    void persistPositiveArbitrageRecords(List<TableTennisEventWrapperDTO> eventWrapperList);
}
