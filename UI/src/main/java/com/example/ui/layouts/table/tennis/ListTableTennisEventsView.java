package com.example.ui.layouts.table.tennis;

import com.example.ui.entities.helpers.TableTennisEventWrapper;
import com.example.ui.layouts.MainLayout;
import com.example.ui.services.NamesSimilaritiesService;
import com.example.ui.services.TableTennisService;
import com.example.ui.services.helpers.CalculatorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Route(value = "tabletennisevents", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListTableTennisEventsView extends VerticalLayout {

    private final Grid<TableTennisEventWrapper> grid = new Grid<>(TableTennisEventWrapper.class, false);

    CalculatingStakeFormView calculatingStakeFormView;

    TableTennisEventWrapper selectedRow;

    public ListTableTennisEventsView(@Autowired TableTennisService tableTennisService,
                                     @Autowired NamesSimilaritiesService namesSimilaritiesService,
                                     @Autowired CalculatorService calculatorService) {

        List<TableTennisEventWrapper> tableTennisEvents = tableTennisService.getData();
        List<String> platformNames = tableTennisService.getPlatformNames();

        configureGrid(tableTennisEvents, platformNames);

        AddNamesSimilaritiesFormView addNamesSimilaritiesFormView = new AddNamesSimilaritiesFormView(namesSimilaritiesService);

        Button showStakesCalculator = new Button("Show Stakes");
        calculatingStakeFormView = new CalculatingStakeFormView(calculatorService, selectedRow);
        showStakesCalculator.addClickListener(e -> {
            if (this.selectedRow != null) {
                calculatingStakeFormView.setSelected(this.selectedRow);
                calculatingStakeFormView.initCalculatingStakeFormLayout( 100d);
                calculatingStakeFormView.setVisible(true);
            }
        });

        FlexLayout content = configureContent(addNamesSimilaritiesFormView, calculatingStakeFormView);
        Button showAddNewNamesSimilarities = new Button("Add Name Similarity");
        showAddNewNamesSimilarities.addClickListener(e -> addNamesSimilaritiesFormView.setVisible(true));

        addAndExpand(new HorizontalLayout(showAddNewNamesSimilarities, showStakesCalculator), content);
        setHeightFull();
    }

    private FlexLayout configureContent(AddNamesSimilaritiesFormView addNamesSimilaritiesFormView, CalculatingStakeFormView calculatingStakeFormView) {
        calculatingStakeFormView.setWidth("6em");
        calculatingStakeFormView.setVisible(false);

        addNamesSimilaritiesFormView.setWidth("6em");
        addNamesSimilaritiesFormView.setVisible(false);

        FlexLayout content = new FlexLayout(grid, addNamesSimilaritiesFormView, calculatingStakeFormView);
        content.setFlexGrow(4, grid);
        content.setFlexGrow(1, addNamesSimilaritiesFormView);
        content.setFlexGrow(1, calculatingStakeFormView);
        content.setFlexShrink(0, addNamesSimilaritiesFormView);
        content.setFlexShrink(0, calculatingStakeFormView);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();
        return content;
    }


    private void configureGrid(List<TableTennisEventWrapper> tableTennisEvents, List<String> platformNames) {
        grid.addClassNames("ttee-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addColumn(е -> е.getTableTennisEventEntityShort().getEventDate()).setHeader("Date");
        grid.addColumn(е -> е.getTableTennisEventEntityShort().getFirstPlayer()).setHeader("First Player");
        grid.addColumn(е -> е.getTableTennisEventEntityShort().getSecondPlayer()).setHeader("Second Player");
        grid.addColumn(TableTennisEventWrapper::getArbitragePercentage).setHeader("Arb N");

        for (String platformName : platformNames) {
            grid.addColumn(e -> e.getEventEntityMap().getOrDefault(platformName, null) == null ?
                    "" : e.getEventEntityMap().getOrDefault(platformName, null).toString()).setHeader(platformName);
        }

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addSelectionListener(event -> {
            Optional<TableTennisEventWrapper> first = event.getAllSelectedItems().stream().findFirst();
            first.ifPresent(tableTennisEventWrapper -> selectedRow = tableTennisEventWrapper);
            calculatingStakeFormView.setSelected(this.selectedRow);
        });

        grid.setVisible(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(tableTennisEvents);
        grid.setHeightFull();
    }
}
