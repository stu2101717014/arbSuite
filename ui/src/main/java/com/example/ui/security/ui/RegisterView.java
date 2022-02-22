package com.example.ui.security.ui;

import com.example.ui.security.data.User;
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
@Route(value = RegisterView.ROUTE)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegisterView extends VerticalLayout implements BeforeEnterObserver {

    public static final String ROUTE = "register";

    private final IUserService userService;

    private TextField username;

    private EmailField emailField;

    private PasswordField passwordField;

    private Button register;

    @Autowired
    public RegisterView(IUserService userService) {
        this.userService = userService;

        this.username = new TextField();
        this.username.setLabel("Username");
        this.emailField = new EmailField();
        this.emailField.setLabel("Email");
        this.passwordField = new PasswordField();
        this.passwordField.setLabel("Password");
        this.register = new Button("Register");
        this.register.addClickListener(this::registerUser);

        add(this.username, this.emailField, this.passwordField, this.register);
        setSizeFull();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }


    private void registerUser(ClickEvent<Button> e) {
        User user = new User();
        user.setName(this.username.getValue());
        user.setEmail(this.emailField.getValue());
        user.setPassword(this.passwordField.getValue());
        user.setRoles("ROLE_User");
        this.userService.saveUser(user);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    }
}
