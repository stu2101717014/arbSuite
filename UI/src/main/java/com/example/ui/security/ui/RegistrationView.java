package com.example.ui.security.ui;

import com.example.ui.layouts.MainLayout;
import com.example.ui.security.configs.SecurityConfig;
import com.example.ui.security.services.IUserService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Route(value = "registration")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegistrationView extends VerticalLayout {

    private final IUserService userService;

    private TextField username;

    private EmailField emailField;

    private PasswordField passwordField;

    private PasswordField confirmPassword;

    private Button register;

    @Autowired
    public RegistrationView(IUserService userService) {
        this.userService = userService;

        this.username = new TextField();
        this.emailField = new EmailField();
        this.passwordField = new PasswordField();
        this.confirmPassword = new PasswordField();
        this.register = new Button("Register");
        this.register.addClickListener(this::registerUser);

        add(this.username, this.emailField, this.passwordField, this.confirmPassword, this.register);

    }


    private void registerUser(ClickEvent<Button> e) {

        String dbg = "";
    }

}
