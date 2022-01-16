package com.example.ui;

import com.example.ui.entities.FourPlatformsEventWrapper;
import com.example.ui.services.UIService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;

@Component
@Route(value="", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListTableTennisEvents extends VerticalLayout {

    private final Grid<FourPlatformsEventWrapper> grid = new Grid<>(FourPlatformsEventWrapper.class, false);

    public ListTableTennisEvents(@Autowired UIService uiService) {

        List<FourPlatformsEventWrapper> tableTennisEvents = uiService.getData();

        grid.addClassNames("ttee-grid");
        grid.addColumn(FourPlatformsEventWrapper::getEventDate).setHeader("Date");
        grid.addColumn(FourPlatformsEventWrapper::getFirstPlayer).setHeader("First Player Name");
        grid.addColumn(FourPlatformsEventWrapper::getSecondPlayer).setHeader("Second Player Name");
        grid.addColumn(FourPlatformsEventWrapper::getBwinEvent).setHeader("Bwin");
        grid.addColumn(FourPlatformsEventWrapper::getBetWinnerEvent).setHeader("BetWinner");
        grid.addColumn(FourPlatformsEventWrapper::getBets22Event).setHeader("Bets22");
        grid.addColumn(FourPlatformsEventWrapper::getBet365Event).setHeader("Bet365");

        grid.setVisible(true);
        grid.getColumns().forEach(col -> col.setWidth("250px"));

        grid.setItems(tableTennisEvents);
        //grid.setSizeFull();

        addAndExpand(grid);
    }
}
