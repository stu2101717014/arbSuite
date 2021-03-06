package com.example.ui.views.table.tennis;

import com.example.ui.services.interfaces.NamesSimilaritiesService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import dtos.NamesSimilaritiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddNamesSimilaritiesFormView extends VerticalLayout {

    private final TextField playerPlatformSpecificName;
    private final TextField playerUniversalName;
    private final TextField platformName;

    private final NamesSimilaritiesService namesSimilaritiesService;

    @Autowired
    public AddNamesSimilaritiesFormView(NamesSimilaritiesService namesSimilaritiesService) {
        this.namesSimilaritiesService = namesSimilaritiesService;

        playerPlatformSpecificName = new TextField();
        playerPlatformSpecificName.setLabel("Platform Specific Name");

        playerUniversalName = new TextField();
        playerUniversalName.setLabel("Player Universal Name");

        platformName = new TextField();
        platformName.setLabel("Platform Name");

        Button addNameSimilarity = new Button("Add");
        addNameSimilarity.addClickListener(this::addNameSimilarity);

        Button hide = new Button("Hide");
        hide.addClickListener(e -> this.setVisible(false));

        add(playerPlatformSpecificName, playerUniversalName, platformName, addNameSimilarity, hide);
    }


    private void addNameSimilarity(ClickEvent<Button> event) {
        String playerPlatformSpecificNameAsString = playerPlatformSpecificName.getValue();
        String playerUniversalNameAsString = playerUniversalName.getValue();
        String platformNameAsString = platformName.getValue();

        NamesSimilaritiesDTO namesSimilaritiesDAO = new NamesSimilaritiesDTO();
        namesSimilaritiesDAO.setUniversalPlayerName(playerUniversalNameAsString);
        namesSimilaritiesDAO.setPlatformName(platformNameAsString);
        namesSimilaritiesDAO.setPlatformSpecificPlayerName(playerPlatformSpecificNameAsString);

        this.namesSimilaritiesService.saveAndFlushNameSimilarity(namesSimilaritiesDAO);
        playerPlatformSpecificName.setValue("");
        playerUniversalName.setValue("");
        platformName.setValue("");

        this.setVisible(false);
    }
}
