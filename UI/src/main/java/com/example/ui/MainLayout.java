package com.example.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createDrawer();
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Table Tennis Events", ListTableTennisEventsLayout.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink apis = new RouterLink("APIs", ApisLayout.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink,
                apis
        ));

    }
}
