package com.example.ui.views.names.similarities;

import com.example.ui.entities.jpa.NamesSimilarities;
import com.example.ui.services.NamesSimilaritiesService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class NamesSimilaritiesFormView extends VerticalLayout {

    private Button newNameSimilarity;
    private Button save;
    private Button delete;

    private TextField platformName;
    private TextField platformSpecificPlayerName;
    private TextField universalPlayerName;

    private NamesSimilarities selected;
    private NamesSimilaritiesService namesSimilaritiesService;

    private final Grid<NamesSimilarities> gridRef;

    public NamesSimilaritiesFormView(NamesSimilaritiesService namesSimilaritiesService, Grid<NamesSimilarities> grid) {
        this.gridRef = grid;
        this.namesSimilaritiesService =  namesSimilaritiesService;
        platformName = new TextField();
        platformName.setLabel("Platform name");

        platformSpecificPlayerName = new TextField();
        platformSpecificPlayerName.setLabel("Platform Specific Name");

        universalPlayerName = new TextField();
        universalPlayerName.setLabel("Universal Name");

        newNameSimilarity = new Button("New");
        save = new Button("Save");
        delete = new Button("Delete");

        newNameSimilarity.addClickListener(this::addNewNameSimilarityButtonClicked);
        save.addClickListener(this::saveButtonClicked);
        delete.addClickListener(this::deleteButtonClicked);

        add(platformName, platformSpecificPlayerName, universalPlayerName, newNameSimilarity, save, delete);
        this.setVisible(false);
    }

    public void setNamesSimilarities(NamesSimilarities namesSimilarities){
        if (namesSimilarities == null){
            return;
        }
        this.selected = namesSimilarities;
        platformName.setValue(this.selected.getPlatformName());
        platformSpecificPlayerName.setValue(this.selected.getPlatformSpecificPlayerName());
        universalPlayerName.setValue(this.selected.getUniversalPlayerName());
    }

    private void deleteButtonClicked(ClickEvent<Button> buttonClickEvent) {
        if (selected != null){
            namesSimilaritiesService.deleteNameSimilarity(selected);
        }
        this.setVisible(false);
        this.refreshGridData();
    }

    private void saveButtonClicked(ClickEvent<Button> buttonClickEvent) {
        NamesSimilarities namesSimilarities = new NamesSimilarities();
        namesSimilarities.setPlatformSpecificPlayerName(platformSpecificPlayerName.getValue());
        namesSimilarities.setPlatformName(platformName.getValue());
        namesSimilarities.setUniversalPlayerName(universalPlayerName.getValue());

        namesSimilaritiesService.saveAndFlushNameSimilarity(namesSimilarities);
        this.setVisible(false);
        this.refreshGridData();
    }

    private void addNewNameSimilarityButtonClicked(ClickEvent<Button> event) {
        platformName.setValue("");
        platformSpecificPlayerName.setValue("");
        universalPlayerName.setValue("");
    }

    private void refreshGridData(){
        this.gridRef.setItems(this.namesSimilaritiesService.getAll());
        this.gridRef.getDataProvider().refreshAll();
    }
}
