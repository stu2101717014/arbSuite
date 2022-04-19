package com.example.ui.services.interfaces;

import java.util.List;

public interface ArbitrageService {

    List<Double> calculateBets(double investment, List<Double> odds, double arbitragePercentage);

}
