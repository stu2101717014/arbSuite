package com.example.ui.views.table.tennis;

import com.example.ui.security.utils.SecurityUtils;
import com.example.ui.services.interfaces.TableTennisService;
import com.example.ui.views.MainLayout;
import com.example.ui.security.utils.SecuredByRole;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


import dtos.MetricsDTO;
import dtos.PostProcessTableTennisWrapperDTO;
import dtos.TableTennisEventWrapperDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SecuredByRole(SecurityUtils.ROLE_USER)
@Component
@Route(value = "tabletennisevents", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListTableTennisEventsView extends VerticalLayout {

    private final Grid<TableTennisEventWrapperDTO> grid =
            new Grid<>(TableTennisEventWrapperDTO.class, false);

    private final TableTennisService tableTennisService;

    private final CalculatingStakeFormView calculatingStakeFormView;

    private TableTennisEventWrapperDTO selectedRow;

    public static final Double INITIAL_AMOUNT = 100d;

    public final Authentication auth;

    @Autowired
    public ListTableTennisEventsView(TableTennisService tableTennisService,
                                     CalculatingStakeFormView calculatingStakeFormView,
                                     AddNamesSimilaritiesFormView addNamesSimilaritiesFormView) {

        this.auth = SecurityContextHolder.getContext().getAuthentication();


        this.tableTennisService = tableTennisService;

        List<TableTennisEventWrapperDTO> tableTennisEvents = getTableTennisProcessedData(this.tableTennisService);

        List<String> platformNames = tableTennisService.getPlatformNames();

        configureGrid(tableTennisEvents, platformNames);

        Button showStakesCalculator = new Button("Show Stakes");

        this.calculatingStakeFormView = calculatingStakeFormView;
        this.calculatingStakeFormView.setSelected(this.selectedRow);

        showStakesCalculator.addClickListener(e -> {
            if (this.selectedRow != null) {
                calculatingStakeFormView.setSelected(this.selectedRow);
                calculatingStakeFormView.initCalculatingStakeFormLayout(INITIAL_AMOUNT);
                calculatingStakeFormView.setVisible(true);
            }
        });

        FlexLayout content = configureContent(addNamesSimilaritiesFormView, calculatingStakeFormView);

        Collection<? extends GrantedAuthority> authorities = this.auth.getAuthorities();
        List<String> stringStream = authorities.stream().map(a -> a.getAuthority().toString()).filter(a -> a.equals(SecurityUtils.ROLE_ADMIN)).collect(Collectors.toList());

        if (stringStream.size() > 0) {
            Button showAddNewNamesSimilarities = new Button("Add Name Similarity");
            showAddNewNamesSimilarities.addClickListener(e -> addNamesSimilaritiesFormView.setVisible(true));
            addAndExpand(new HorizontalLayout(showAddNewNamesSimilarities, showStakesCalculator), content, getMetrics());
        } else {
            addAndExpand(new HorizontalLayout(showStakesCalculator), content);
        }
        setHeightFull();
    }

    private VerticalLayout getMetrics() {
        MetricsDTO metrics = this.tableTennisService.getMetrics();
        VerticalLayout verticalLayout = new VerticalLayout();
        Label reshapeMetricLabel = new Label("Reshape data time : " + metrics.getDataReshapeTimeComplexity() + " ms");
        Label remapNamesMetricLabel = new Label("Remap names time : " + metrics.getNameSimilaritiesRemapTimeComplexity() + " ms");
        verticalLayout.add(remapNamesMetricLabel);
        verticalLayout.add(reshapeMetricLabel);

        verticalLayout.getElement().getStyle().set("position", "absolute");
        verticalLayout.getElement().getStyle().set("bottom", 20 + "px");
        verticalLayout.getElement().getStyle().set("left", 20 + "px");

        return verticalLayout;
    }


    private List<TableTennisEventWrapperDTO> getTableTennisProcessedData(TableTennisService tableTennisService) {
        PostProcessTableTennisWrapperDTO processedData = tableTennisService.getProcessedData();

        List<TableTennisEventWrapperDTO> tableTennisEvents = new Gson().fromJson(processedData.getResultAsJson(),
                new TypeToken<ArrayList<TableTennisEventWrapperDTO>>() {
                }.getType());

        tableTennisEvents = tableTennisService.sortReshapedData(tableTennisEvents);
        return tableTennisEvents;
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
