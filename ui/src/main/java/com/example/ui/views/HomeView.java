package com.example.ui.views;

import com.example.ui.security.utils.SecuredByRole;
import com.example.ui.security.utils.SecurityUtils;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SecuredByRole(SecurityUtils.ROLE_USER)
@Component
@Route(value = "", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HomeView extends VerticalLayout {

    private final Label welcome;

    public HomeView() {
        this.welcome = new Label("Welcome user");
        add(this.welcome);
    }
}
