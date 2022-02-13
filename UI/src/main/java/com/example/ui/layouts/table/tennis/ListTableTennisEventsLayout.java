package com.example.ui.layouts.table.tennis;

import com.example.ui.entities.helpers.TableTennisEventWrapper;
import com.example.ui.entities.jpa.NamesSimilarities;
import com.example.ui.layouts.MainLayout;
import com.example.ui.services.NamesSimilaritiesService;
import com.example.ui.services.TableTennisService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Route(value = "TableTennisEvents", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListTableTennisEventsLayout extends VerticalLayout {

    private final Grid<TableTennisEventWrapper> grid = new Grid<>(TableTennisEventWrapper.class, false);

    public ListTableTennisEventsLayout(@Autowired TableTennisService tableTennisService, @Autowired NamesSimilaritiesService namesSimilaritiesService) {

        List<TableTennisEventWrapper> tableTennisEvents = tableTennisService.getData();
        List<String> platformNames = tableTennisService.getPlatformNames();

        configureGrid(tableTennisEvents, platformNames);

        AddNamesSimilaritiesFormLayout addNamesSimilaritiesFormLayout = new AddNamesSimilaritiesFormLayout(namesSimilaritiesService);

        FlexLayout content = configureContent(addNamesSimilaritiesFormLayout);
        Button showAddNewNamesSimilarities = new Button("Add Name Similarity");
        showAddNewNamesSimilarities.addClickListener(e -> addNamesSimilaritiesFormLayout.setVisible(true));

        addAndExpand(new HorizontalLayout(showAddNewNamesSimilarities), content);
        setHeightFull();
    }

    private FlexLayout configureContent(AddNamesSimilaritiesFormLayout addNamesSimilaritiesFormLayout) {
        addNamesSimilaritiesFormLayout.setWidth("6em");
        addNamesSimilaritiesFormLayout.setVisible(false);

        FlexLayout content = new FlexLayout(grid, addNamesSimilaritiesFormLayout);
        content.setFlexGrow(4, grid);
        content.setFlexGrow(1, addNamesSimilaritiesFormLayout);
        content.setFlexShrink(0, addNamesSimilaritiesFormLayout);
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

        grid.setVisible(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(tableTennisEvents);
        grid.setHeightFull();
    }
}
