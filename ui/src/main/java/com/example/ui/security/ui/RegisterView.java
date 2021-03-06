package com.example.ui.security.ui;

import com.example.ui.security.data.User;
import com.example.ui.security.services.UserService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@Route(value = RegisterView.ROUTE)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegisterView extends VerticalLayout {

    public static final String ROUTE = "register";

    private final UserService userService;

    private final H1 registerLabel;
    private final TextField username;

    private final EmailField emailField;

    private final PasswordField passwordField;

    @Autowired
    public RegisterView(UserService userService) {
        this.userService = userService;

        this.registerLabel = new H1();
        this.registerLabel.setText("Register");
        this.username = new TextField();
        this.username.setLabel("Username");
        this.emailField = new EmailField();
        this.emailField.setLabel("Email");
        this.passwordField = new PasswordField();
        this.passwordField.setLabel("Password");
        Button register = new Button("Register");
        register.addClickListener(this::registerUser);

        add(this.registerLabel, this.username, this.emailField, this.passwordField, register);
        setSizeFull();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


    }


    private void registerUser(ClickEvent<Button> e) {
        User user = new User();
        user.setName(this.username.getValue());
        user.setEmail(this.emailField.getValue());
        user.setPassword(this.passwordField.getValue());
        this.userService.setDefaultRole(user);
        this.userService.saveUser(user);

        e.getSource().getUI().ifPresent(ie -> ie.getPage().setLocation("/login"));
    }

}
