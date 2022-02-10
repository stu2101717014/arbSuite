package com.example.ui.layouts;

import com.example.ui.entities.jpa.NamesSimilarities;
import com.example.ui.services.NamesSimilaritiesService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Route(value="NamesSimilarities", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NamesSimilaritiesLayout extends VerticalLayout {

    private final Grid<NamesSimilarities> grid = new Grid<>(NamesSimilarities.class, true);

    private NamesSimilaritiesService namesSimilaritiesService;

    private NamesSimilarities selected;

    private TextField platformName;
    private TextField platformSpecificPlayerName;
    private TextField universalPlayerName;

    private Button newNameSimilarity;
    private Button save;
    private Button delete;

    public NamesSimilaritiesLayout(@Autowired NamesSimilaritiesService namesSimilaritiesService) {
        this.namesSimilaritiesService = namesSimilaritiesService;
        grid.setItems(namesSimilaritiesService.getAll());
        grid.addSelectionListener(this::onNameSimilaritiesSelected);
        grid.asSingleSelect();
        HorizontalLayout formLayout = configureForm();

        addAndExpand(grid);
        add(formLayout);
    }

    private void onNameSimilaritiesSelected(SelectionEvent<Grid<NamesSimilarities>, NamesSimilarities> event) {
        if (event.getFirstSelectedItem().isPresent()){
            NamesSimilarities firstSelectedItem = event.getFirstSelectedItem().get();

            platformName.setValue(firstSelectedItem.getPlatformName());
            platformSpecificPlayerName.setValue(firstSelectedItem.getPlatformSpecificPlayerName());
            universalPlayerName.setValue(firstSelectedItem.getUniversalPlayerName());

            selected = firstSelectedItem;
        }
    }

    private HorizontalLayout configureForm() {
        HorizontalLayout layout = new HorizontalLayout();

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

        layout.add(platformName, platformSpecificPlayerName, universalPlayerName, newNameSimilarity, save, delete);
        return layout;
    }

    private void deleteButtonClicked(ClickEvent<Button> buttonClickEvent) {
        if (selected != null){
            namesSimilaritiesService.deleteNameSimilarity(selected);
            grid.setItems(namesSimilaritiesService.getAll());
            grid.getDataProvider().refreshAll();
        }
    }

    private void saveButtonClicked(ClickEvent<Button> buttonClickEvent) {
        NamesSimilarities namesSimilarities = new NamesSimilarities();
        namesSimilarities.setPlatformSpecificPlayerName(platformSpecificPlayerName.getValue());
        namesSimilarities.setPlatformName(platformName.getValue());
        namesSimilarities.setUniversalPlayerName(universalPlayerName.getValue());

        namesSimilaritiesService.saveAndFlushNameSimilarity(namesSimilarities);
        grid.setItems(namesSimilaritiesService.getAll());
        grid.getDataProvider().refreshAll();
    }

    private void addNewNameSimilarityButtonClicked(ClickEvent<Button> event) {
        platformName.setValue("");
        platformSpecificPlayerName.setValue("");
        universalPlayerName.setValue("");
    }
}
