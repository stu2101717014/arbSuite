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
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Route(value = "TableTennisEvents", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListTableTennisEventsLayout extends VerticalLayout {

    private final Grid<TableTennisEventWrapper> grid = new Grid<>(TableTennisEventWrapper.class, false);

    CalculatingStakeFormLayout calculatingStakeFormLayout;

    TableTennisEventWrapper selectedRow;

    public ListTableTennisEventsLayout(@Autowired TableTennisService tableTennisService,
                                       @Autowired NamesSimilaritiesService namesSimilaritiesService,
                                       @Autowired CalculatorService calculatorService) {

        List<TableTennisEventWrapper> tableTennisEvents = tableTennisService.getData();
        List<String> platformNames = tableTennisService.getPlatformNames();

        configureGrid(tableTennisEvents, platformNames);

        AddNamesSimilaritiesFormLayout addNamesSimilaritiesFormLayout = new AddNamesSimilaritiesFormLayout(namesSimilaritiesService);

        Button showStakesCalculator = new Button("Show Stakes");
        calculatingStakeFormLayout = new CalculatingStakeFormLayout(calculatorService, selectedRow);
        showStakesCalculator.addClickListener(e -> {
            if (this.selectedRow != null) {
                calculatingStakeFormLayout.setSelected(this.selectedRow);
                calculatingStakeFormLayout.initCalculatingStakeFormLayout( 100d);
                calculatingStakeFormLayout.setVisible(true);
            }
        });

        FlexLayout content = configureContent(addNamesSimilaritiesFormLayout, calculatingStakeFormLayout);
        Button showAddNewNamesSimilarities = new Button("Add Name Similarity");
        showAddNewNamesSimilarities.addClickListener(e -> addNamesSimilaritiesFormLayout.setVisible(true));

        addAndExpand(new HorizontalLayout(showAddNewNamesSimilarities, showStakesCalculator), content);
        setHeightFull();
    }

    private FlexLayout configureContent(AddNamesSimilaritiesFormLayout addNamesSimilaritiesFormLayout, CalculatingStakeFormLayout calculatingStakeFormLayout) {
        calculatingStakeFormLayout.setWidth("6em");
        calculatingStakeFormLayout.setVisible(false);

        addNamesSimilaritiesFormLayout.setWidth("6em");
        addNamesSimilaritiesFormLayout.setVisible(false);

        FlexLayout content = new FlexLayout(grid, addNamesSimilaritiesFormLayout, calculatingStakeFormLayout);
        content.setFlexGrow(4, grid);
        content.setFlexGrow(1, addNamesSimilaritiesFormLayout);
        content.setFlexGrow(1, calculatingStakeFormLayout);
        content.setFlexShrink(0, addNamesSimilaritiesFormLayout);
        content.setFlexShrink(0, calculatingStakeFormLayout);
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
            calculatingStakeFormLayout.setSelected(this.selectedRow);
        });

        grid.setVisible(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(tableTennisEvents);
        grid.setHeightFull();
    }
}
