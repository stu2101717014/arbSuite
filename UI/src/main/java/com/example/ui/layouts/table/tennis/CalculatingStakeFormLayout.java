package com.example.ui.layouts.table.tennis;

import com.example.ui.entities.helpers.TableTennisEventEntity;
import com.example.ui.entities.helpers.TableTennisEventWrapper;
import com.example.ui.services.helpers.CalculatorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.*;

public class CalculatingStakeFormLayout extends VerticalLayout {

    private Label amount;

    private Label playerOneName;
    private Label playerTwoName;

    private Label playerOneOdd;
    private Label playerTwoOdd;

    private Label platformOne;
    private Label platformTwo;

    private Label total;

    private Button hideForm;
    private CalculatorService calculatorService;

    public CalculatingStakeFormLayout(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
        this.amount = new Label();
        this.playerOneName = new Label();
        this.playerTwoName = new Label();

        this.playerOneOdd = new Label();
        this.playerTwoOdd = new Label();

        this.platformOne = new Label();
        this.platformTwo = new Label();
        this.total =  new Label();

        this.hideForm = new Button("Hide Form");
        this.hideForm.addClickListener(e -> this.setVisible(false));
        add(amount, playerOneName, playerTwoName, playerOneOdd, playerTwoOdd, platformOne, platformTwo, total, hideForm);
    }

    public void initCalculatingStakeFormLayout(TableTennisEventWrapper tableTennisEventWrapper, double amount){

        this.amount.setText(String.valueOf(amount));

        this.playerOneName.setText(tableTennisEventWrapper.getTableTennisEventEntityShort().getFirstPlayer());
        this.playerTwoName.setText(tableTennisEventWrapper.getTableTennisEventEntityShort().getSecondPlayer());

        List<Double> winningOdds = new ArrayList<Double>();

        Optional<TableTennisEventEntity> firstPlayerTTEE = tableTennisEventWrapper.getEventEntityMap().values().stream()
                .max(Comparator.comparing(TableTennisEventEntity::getFirstPlayerWinningOdd));
        firstPlayerTTEE.ifPresent(ttee -> {
            this.playerOneOdd.setText("Highest odd : " + ttee.getFirstPlayerWinningOdd().toString());
            winningOdds.add(ttee.getFirstPlayerWinningOdd());
        });


        Optional<TableTennisEventEntity> secondPlayerTTEE = tableTennisEventWrapper.getEventEntityMap().values().stream()
                .max(Comparator.comparing(TableTennisEventEntity::getSecondPlayerWinningOdd));
        secondPlayerTTEE.ifPresent(ttee -> {
            this.playerTwoOdd.setText("Highest odd : " + ttee.getSecondPlayerWinningOdd().toString());
            winningOdds.add(ttee.getSecondPlayerWinningOdd());
        });

        List<Double> calculatedSingleBets = this.calculatorService
                .calculateBets(amount, winningOdds, tableTennisEventWrapper.getArbitragePercentage());

        this.platformOne.setText("Platform : " + tableTennisEventWrapper.getWinningPlatformOne() + " : "
                + String.format("Bet : %.2f", calculatedSingleBets.get(0)));
        this.platformTwo.setText("Platform : " + tableTennisEventWrapper.getWinningPlatformTwo()+ " : "
                + String.format("Bet : %.2f", calculatedSingleBets.get(1)));

        if (firstPlayerTTEE.isPresent() && secondPlayerTTEE.isPresent()){
            double _total = (firstPlayerTTEE.get().getFirstPlayerWinningOdd() * calculatedSingleBets.get(0)
                    + secondPlayerTTEE.get().getSecondPlayerWinningOdd() * calculatedSingleBets.get(1)) / 2;

            this.total.setText(String.format("Expected win : %.2f", _total ));
        }
    }
}
