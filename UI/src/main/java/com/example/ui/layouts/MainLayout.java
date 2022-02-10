package com.example.ui.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createDrawer();
    }

    private void createDrawer() {
        RouterLink tableTennisEvents = new RouterLink("Table Tennis Events", ListTableTennisEventsLayout.class);
        tableTennisEvents.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink apis = new RouterLink("APIs", ApisLayout.class);
        apis.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink namesSimilarities = new RouterLink("Names Similarities", NamesSimilaritiesLayout.class);
        namesSimilarities.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                apis,
                namesSimilarities,
                tableTennisEvents

        ));

    }
}
