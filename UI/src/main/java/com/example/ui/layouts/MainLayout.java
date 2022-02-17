package com.example.ui.layouts;

import com.example.ui.layouts.names.similarities.NamesSimilaritiesView;
import com.example.ui.layouts.table.tennis.ListTableTennisEventsView;
import com.example.ui.security.services.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H5 logo = new H5("");
        logo.addClassNames("text-l", "m-m");

        Button logout = new Button("Log out", e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void createDrawer() {
        RouterLink tableTennisEvents = new RouterLink("Table Tennis Events", ListTableTennisEventsView.class);
        tableTennisEvents.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink apis = new RouterLink("APIs", ApisView.class);
        apis.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink namesSimilarities = new RouterLink("Names Similarities", NamesSimilaritiesView.class);
        namesSimilarities.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                apis,
                namesSimilarities,
                tableTennisEvents

        ));

    }

}
