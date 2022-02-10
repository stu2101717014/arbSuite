package com.example.ui.layouts;

import com.example.ui.entities.helpers.TableTennisEventWrapper;
import com.example.ui.entities.jpa.NamesSimilarities;
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
@Route(value="TableTennisEvents", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListTableTennisEventsLayout extends VerticalLayout {

    private final Grid<TableTennisEventWrapper> grid = new Grid<>(TableTennisEventWrapper.class, false);

    private final NamesSimilaritiesService namesSimilaritiesService;

    private TextField playerPlatformSpecificName;
    private TextField playerUniversalName;
    private TextField platformName;


    public ListTableTennisEventsLayout(@Autowired TableTennisService tableTennisService, @Autowired NamesSimilaritiesService namesSimilaritiesService) {
        this.namesSimilaritiesService = namesSimilaritiesService;

        List<TableTennisEventWrapper> tableTennisEvents = tableTennisService.getData();
        List<String> platformNames = tableTennisService.getPlatformNames();

        configureGrid(tableTennisEvents, platformNames);
        HorizontalLayout formLayout = configureForm();

        addAndExpand(grid);
        add(formLayout);
    }

    private HorizontalLayout configureForm(){
        HorizontalLayout layout = new HorizontalLayout();

        playerPlatformSpecificName = new TextField();
        playerPlatformSpecificName.setLabel("Platform Specific Name");

        playerUniversalName = new TextField();
        playerUniversalName.setLabel("Player Universal Name");

        platformName = new TextField();
        platformName.setLabel("Platform Name");

        Button addNameSimilarity = new Button("Add");
        addNameSimilarity.addClickListener(this::addNameSimilarity);

        layout.add(playerPlatformSpecificName, playerUniversalName, platformName, addNameSimilarity);
        return layout;
    }

    private void addNameSimilarity(ClickEvent<Button> event) {
        String playerPlatformSpecificNameAsString = playerPlatformSpecificName.getValue();
        String playerUniversalNameAsString = playerUniversalName.getValue();
        String platformNameAsString = platformName.getValue();

        NamesSimilarities namesSimilarities = new NamesSimilarities();
        namesSimilarities.setUniversalPlayerName(playerUniversalNameAsString);
        namesSimilarities.setPlatformName(platformNameAsString);
        namesSimilarities.setPlatformSpecificPlayerName(playerPlatformSpecificNameAsString);

        this.namesSimilaritiesService.saveAndFlushNameSimilarity(namesSimilarities);
        playerPlatformSpecificName.setValue("");
        playerUniversalName.setValue("");
        platformName.setValue("");
    }

    private void configureGrid(List<TableTennisEventWrapper> tableTennisEvents, List<String> platformNames) {
        grid.addClassNames("ttee-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addColumn(е -> е.getTableTennisEventEntityShort().getEventDate()).setHeader("Date");
        grid.addColumn(е -> е.getTableTennisEventEntityShort().getFirstPlayer()).setHeader("First Player");
        grid.addColumn(е -> е.getTableTennisEventEntityShort().getSecondPlayer()).setHeader("Second Player");
        grid.addColumn(TableTennisEventWrapper::getArbitragePercentage).setHeader("Arb N");

        for (String platformName : platformNames){
            grid.addColumn(e -> e.getEventEntityMap().getOrDefault(platformName, null) == null ?
                    "" : e.getEventEntityMap().getOrDefault(platformName, null).toString()).setHeader(platformName);
        }

        grid.setVisible(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(tableTennisEvents);

    }
}
