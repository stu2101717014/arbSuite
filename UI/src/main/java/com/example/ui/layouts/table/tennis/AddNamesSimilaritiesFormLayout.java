package com.example.ui.layouts.table.tennis;

import com.example.ui.entities.jpa.NamesSimilarities;
import com.example.ui.services.NamesSimilaritiesService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;


public class AddNamesSimilaritiesFormLayout extends VerticalLayout {

    private TextField playerPlatformSpecificName;
    private TextField playerUniversalName;
    private TextField platformName;

    private final NamesSimilaritiesService namesSimilaritiesService;

    public AddNamesSimilaritiesFormLayout(NamesSimilaritiesService namesSimilaritiesService) {
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

        NamesSimilarities namesSimilarities = new NamesSimilarities();
        namesSimilarities.setUniversalPlayerName(playerUniversalNameAsString);
        namesSimilarities.setPlatformName(platformNameAsString);
        namesSimilarities.setPlatformSpecificPlayerName(playerPlatformSpecificNameAsString);

        this.namesSimilaritiesService.saveAndFlushNameSimilarity(namesSimilarities);
        playerPlatformSpecificName.setValue("");
        playerUniversalName.setValue("");
        platformName.setValue("");

        this.setVisible(false);
    }
}
