package com.example.ui.security.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Route(value=LoginView.ROUTE)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    public static final String ROUTE = "login";

    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);

        add(new H1("Login"), login, new RouterLink("Register", RegisterView.class));

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        login.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }

}
