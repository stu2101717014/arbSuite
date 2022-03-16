package com.example.ui.views.table.tennis;

import com.example.ui.entities.helpers.TableTennisEventWrapperDTO;
import com.example.ui.entities.jpa.PostProcessTableTennisWrapperDAO;
import com.example.ui.services.interfaces.NamesSimilaritiesService;
import com.example.ui.services.interfaces.TableTennisService;
import com.example.ui.views.MainLayout;
import com.example.ui.security.utils.SecuredByRole;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SecuredByRole("ROLE_Admin")
@Component
@Route(value = "tabletennisevents", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListTableTennisEventsView extends VerticalLayout {

    private final Grid<TableTennisEventWrapperDTO> grid =
            new Grid<>(TableTennisEventWrapperDTO.class, false);

    private final CalculatingStakeFormView calculatingStakeFormView;

    private TableTennisEventWrapperDTO selectedRow;

    public static final Double INITIAL_AMOUNT = 100d;

    public ListTableTennisEventsView(@Autowired NamesSimilaritiesService namesSimilaritiesService,
                                     @Autowired TableTennisService tableTennisService,
                                     @Autowired CalculatingStakeFormView calculatingStakeFormView) {

        PostProcessTableTennisWrapperDAO processedData = tableTennisService.getProcessedData();

        List<TableTennisEventWrapperDTO> tableTennisEvents = new Gson().fromJson(processedData.getResultAsJson(),
                new TypeToken<ArrayList<TableTennisEventWrapperDTO>>(){}.getType());

        tableTennisEvents = tableTennisService.sortReshapedData(tableTennisEvents);

        List<String> platformNames = tableTennisService.getPlatformNames();

        configureGrid(tableTennisEvents, platformNames);

        AddNamesSimilaritiesFormView addNamesSimilaritiesFormView =
                new AddNamesSimilaritiesFormView(namesSimilaritiesService);

        Button showStakesCalculator = new Button("Show Stakes");

        this.calculatingStakeFormView = calculatingStakeFormView;
        this.calculatingStakeFormView.setSelected(this.selectedRow);

        showStakesCalculator.addClickListener(e -> {
            if (this.selectedRow != null) {
                calculatingStakeFormView.setSelected(this.selectedRow);
                calculatingStakeFormView.initCalculatingStakeFormLayout(INITIAL_AMOUNT );
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


    private void configureGrid(List<TableTennisEventWrapperDTO> tableTennisEvents, List<String> platformNames) {
        grid.addClassNames("ttee-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addColumn(е -> е.getTableTennisEventEntityShort().getEventDate()).setHeader("Date");
        grid.addColumn(е -> е.getTableTennisEventEntityShort().getFirstPlayer()).setHeader("First Player");
        grid.addColumn(е -> е.getTableTennisEventEntityShort().getSecondPlayer()).setHeader("Second Player");
        grid.addColumn(TableTennisEventWrapperDTO::getArbitragePercentage).setHeader("Arb N").setSortable(true);

        for (String platformName : platformNames) {
            grid.addColumn(e -> e.getEventEntityMap().getOrDefault(platformName, null) == null ?
                    "" : e.getEventEntityMap().getOrDefault(platformName, null).toString()).setHeader(platformName);
        }

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addSelectionListener(event -> {
            Optional<TableTennisEventWrapperDTO> first = event.getAllSelectedItems().stream().findFirst();
            first.ifPresent(tableTennisEventWrapperDTO -> selectedRow = tableTennisEventWrapperDTO);
            calculatingStakeFormView.setSelected(this.selectedRow);
        });

        grid.setVisible(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(tableTennisEvents);
        grid.setHeightFull();
    }
}
