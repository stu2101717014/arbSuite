package com.example.ui.views;

import com.example.ui.security.utils.SecurityUtils;
import com.example.ui.views.names.similarities.NamesSimilaritiesView;
import com.example.ui.views.table.tennis.ListTableTennisEventsView;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public final Authentication auth;

    public MainLayout(SecurityService securityService) {

        this.auth = SecurityContextHolder.getContext().getAuthentication();
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

        Collection<? extends GrantedAuthority> authorities = this.auth.getAuthorities();
        List<String> stringStream = authorities.stream().map(a -> a.getAuthority().toString()).filter(a -> a.equals(SecurityUtils.ROLE_ADMIN)).collect(Collectors.toList());

        if (stringStream.size() > 0) {
            RouterLink namesSimilarities = new RouterLink("Names Similarities", NamesSimilaritiesView.class);
            namesSimilarities.setHighlightCondition(HighlightConditions.sameLocation());

            addToDrawer(new VerticalLayout(
                    namesSimilarities,
                    tableTennisEvents

            ));
        } else {

            addToDrawer(new VerticalLayout(
                    tableTennisEvents
            ));
        }


    }

}
