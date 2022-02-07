package com.example.ui;

import com.example.ui.entities.helpers.TableTennisEventWrapper;
import com.example.ui.services.TableTennisService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Route(value="", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListTableTennisEvents extends VerticalLayout {

    private final Grid<TableTennisEventWrapper> grid = new Grid<>(TableTennisEventWrapper.class, false);

    private final Button eval = new Button("Eval");

    private Set<TableTennisEventWrapper> selectedSet;


    private TableTennisService tableTennisService;

    public ListTableTennisEvents(@Autowired TableTennisService tableTennisService) {
        this.tableTennisService = tableTennisService;

        List<TableTennisEventWrapper> tableTennisEvents = tableTennisService.getData();
        List<String> platformNames = tableTennisService.getPlatformNames();
        selectedSet = new HashSet<>();

        configureGrid(tableTennisEvents, platformNames);
        VerticalLayout verticalLayout = configureButtons();

        add(verticalLayout);
        addAndExpand(grid);
    }


    private VerticalLayout configureButtons() {
        VerticalLayout verticalLayout = new VerticalLayout();
        this.eval.addClickListener(this::buttonEvalClicked);
        verticalLayout.add(this.eval);

        verticalLayout.getStyle().set("position","absolute");
        verticalLayout.getStyle().set("bottom",20 + "px");
        verticalLayout.getStyle().set("left",20 + "px");
        return verticalLayout;
    }

    private void buttonEvalClicked(ClickEvent<Button> buttonClickEvent) {


        this.grid.deselectAll();
    }

    private void configureGrid(List<TableTennisEventWrapper> tableTennisEvents, List<String> platformNames) {
        grid.addClassNames("ttee-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addColumn(е -> е.getTableTennisEventEntityShort().getEventDate()).setHeader("Date");
        grid.addColumn(е -> е.getTableTennisEventEntityShort().getSecondPlayer()).setHeader("Second Player");
        grid.addColumn(е -> е.getTableTennisEventEntityShort().getFirstPlayer()).setHeader("First Player");

        for (String platformName : platformNames){
            grid.addColumn(e -> e.getEventEntityMap().getOrDefault(platformName, null) == null ?
                    "" : e.getEventEntityMap().getOrDefault(platformName, null).toString()).setHeader(platformName);
        }

        grid.setVisible(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(tableTennisEvents);

        grid.addSelectionListener(selection -> {
            this.selectedSet = selection.getAllSelectedItems();
        });
    }
}
