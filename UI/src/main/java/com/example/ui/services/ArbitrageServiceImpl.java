package com.example.ui.services;

import com.example.ui.services.interfaces.ArbitrageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArbitrageServiceImpl implements ArbitrageService {

    public List<Double> calculateBets(double investment, List<Double> odds, double arbitragePercentage) {
        double oddsSize = odds.size();
        List<Double> resultList = new ArrayList<>();

        for (int i = 0; i < oddsSize; i++) {
            double individualArbitragePercentage = (1 / odds.get(i)) * 100;
            resultList.add(((investment * individualArbitragePercentage) / arbitragePercentage) / 100);

        }

        return resultList;
    }

}
