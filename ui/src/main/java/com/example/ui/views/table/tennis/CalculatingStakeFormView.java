package com.example.ui.views.table.tennis;

import com.example.ui.services.interfaces.ArbitrageService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import dtos.TableTennisEventEntityDTO;
import dtos.TableTennisEventWrapperDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalculatingStakeFormView extends VerticalLayout {

    private final TextField amount;

    private final Label playerOneName;
    private final Label playerTwoName;

    private final Label playerOneOdd;
    private final Label playerTwoOdd;

    private final Label platformOne;
    private final Label platformTwo;

    private final Label total;

    private final ArbitrageService arbitrageService;

    private TableTennisEventWrapperDTO selected;

    @Autowired
    public CalculatingStakeFormView(ArbitrageService arbitrageService) {
        this.arbitrageService = arbitrageService;
        this.amount = new TextField();
        this.amount.setValue(ListTableTennisEventsView.INITIAL_AMOUNT.toString());
        this.playerOneName = new Label();
        this.playerTwoName = new Label();

        this.playerOneOdd = new Label();
        this.playerTwoOdd = new Label();

        this.platformOne = new Label();
        this.platformTwo = new Label();
        this.total =  new Label();

        Button hideForm = new Button("Hide Form");
        Button reEvaluate = new Button("Reevaluate");
        reEvaluate.addClickListener(e->this.initCalculatingStakeFormLayout(Double.parseDouble(this.amount.getValue())));

        hideForm.addClickListener(e -> this.setVisible(false));
        add(amount, playerOneName, playerTwoName, playerOneOdd, playerTwoOdd, platformOne, platformTwo, total, hideForm, reEvaluate);
    }

    public TableTennisEventWrapperDTO getSelected() {
        return selected;
    }

    public void setSelected(TableTennisEventWrapperDTO selected) {
        this.selected = selected;
    }

    public void initCalculatingStakeFormLayout(double amount){
        if (this.selected == null || this.selected.getArbitragePercentage() == null){
            return;
        }

        this.amount.setValue(String.valueOf(amount));

        this.playerOneName.setText(selected.getTableTennisEventEntityShort().getFirstPlayer());
        this.playerTwoName.setText(selected.getTableTennisEventEntityShort().getSecondPlayer());

        List<Double> winningOdds = new ArrayList<Double>();

        Optional<TableTennisEventEntityDTO> firstPlayerTTEE = selected.getEventEntityMap().values().stream()
                .max(Comparator.comparing(TableTennisEventEntityDTO::getFirstPlayerWinningOdd));
        firstPlayerTTEE.ifPresent(ttee -> {
            this.playerOneOdd.setText("Highest odd : " + ttee.getFirstPlayerWinningOdd().toString());
            winningOdds.add(ttee.getFirstPlayerWinningOdd());
        });


        Optional<TableTennisEventEntityDTO> secondPlayerTTEE = selected.getEventEntityMap().values().stream()
                .max(Comparator.comparing(TableTennisEventEntityDTO::getSecondPlayerWinningOdd));
        secondPlayerTTEE.ifPresent(ttee -> {
            this.playerTwoOdd.setText("Highest odd : " + ttee.getSecondPlayerWinningOdd().toString());
            winningOdds.add(ttee.getSecondPlayerWinningOdd());
        });

        List<Double> calculatedSingleBets = this.arbitrageService
                .calculateBets(amount, winningOdds, selected.getArbitragePercentage());

        this.platformOne.setText("Platform : " + selected.getWinningPlatformOne() + " : "
                + String.format("Bet : %.2f", calculatedSingleBets.get(0)));
        this.platformTwo.setText("Platform : " + selected.getWinningPlatformTwo()+ " : "
                + String.format("Bet : %.2f", calculatedSingleBets.get(1)));

        if (firstPlayerTTEE.isPresent() && secondPlayerTTEE.isPresent()){
            double _total = (firstPlayerTTEE.get().getFirstPlayerWinningOdd() * calculatedSingleBets.get(0)
                    + secondPlayerTTEE.get().getSecondPlayerWinningOdd() * calculatedSingleBets.get(1)) / 2;

            this.total.setText(String.format("Expected win : %.2f", _total ));
        }
    }
}
