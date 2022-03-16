package com.example.ui.views.names.similarities;

import com.example.ui.entities.jpa.NamesSimilaritiesDAO;
import com.example.ui.services.interfaces.NamesSimilaritiesService;
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

    private NamesSimilaritiesDAO selected;
    private NamesSimilaritiesService namesSimilaritiesService;

    private final Grid<NamesSimilaritiesDAO> gridRef;

    public NamesSimilaritiesFormView(NamesSimilaritiesService namesSimilaritiesService, Grid<NamesSimilaritiesDAO> grid) {
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

    public void setNamesSimilarities(NamesSimilaritiesDAO namesSimilaritiesDAO){
        if (namesSimilaritiesDAO == null){
            return;
        }
        this.selected = namesSimilaritiesDAO;
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
        NamesSimilaritiesDAO namesSimilaritiesDAO = new NamesSimilaritiesDAO();
        namesSimilaritiesDAO.setPlatformSpecificPlayerName(platformSpecificPlayerName.getValue());
        namesSimilaritiesDAO.setPlatformName(platformName.getValue());
        namesSimilaritiesDAO.setUniversalPlayerName(universalPlayerName.getValue());

        namesSimilaritiesService.saveAndFlushNameSimilarity(namesSimilaritiesDAO);
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
