package com.example.ui;

import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntity;
import com.example.ui.services.ApisService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Route(value="APIs", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ApisLayout extends HorizontalLayout {

    private final Grid<PlatformDataRequestWrapperEntity> grid = new Grid<>(PlatformDataRequestWrapperEntity.class, false);

    private TextField platformNameTextField;
    private TextField urlTextField;
    private Checkbox checkbox;

    private Button newApiButton;
    private Button delete;
    private Button edit;
    private Button save;

    private PlatformDataRequestWrapperEntity selected;

    private ApisService apisService;

    public ApisLayout(@Autowired ApisService apisService) {
        this.apisService = apisService;
        List<PlatformDataRequestWrapperEntity> all = apisService.getAll();
        configureGrid(all);
        FlexLayout verticalLayout = configureForm();
        addAndExpand(grid);
        add(verticalLayout);
    }

    private FlexLayout configureForm(){
        FlexLayout  flexLayout = new FlexLayout ();
        VerticalLayout verticalLayout = new VerticalLayout();

        platformNameTextField = new TextField();
        platformNameTextField.setLabel("Platform name");
        urlTextField = new TextField();
        urlTextField.setLabel("URL");

        checkbox = new Checkbox();
        checkbox.setLabel("Is Accessible");

        newApiButton = new Button("New");
        newApiButton.addClickListener(event -> addNewApi(event));

        save = new Button("Save");
        save.addClickListener(e -> buttonSaveClicked(e));

        delete = new Button("Delete");
        delete.addClickListener(event -> buttonDeleteClicked(event));

        edit = new Button("Edit");
        edit.addClickListener(event -> buttonEditClicked(event));

        verticalLayout.add(newApiButton, platformNameTextField, urlTextField, checkbox, save, edit, delete);
        flexLayout.add(verticalLayout);
        flexLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        return flexLayout;
    }

    private void buttonEditClicked(ClickEvent<Button> event) {
        if(selected != null){
            selected.setUrl(urlTextField.getValue());
            selected.setPlatformName(platformNameTextField.getValue());
            selected.setAccessible(checkbox.getValue());
            this.apisService.edit(selected);

        }
        grid.deselectAll();
        selected = null;

    }

    private void buttonDeleteClicked(ClickEvent<Button> event) {
        if (selected != null){
            this.apisService.delete(selected);
        }
        this.grid.setItems(apisService.getAll());
        this.grid.getDataProvider().refreshAll();
    }

    private void addNewApi(ClickEvent<Button> event) {
        grid.deselectAll();
        platformNameTextField.setValue("");
        urlTextField.setValue("");
        checkbox.setValue(true);

        this.grid.setItems(apisService.getAll());
        this.grid.getDataProvider().refreshAll();
    }

    private void buttonSaveClicked(ClickEvent<Button> e) {
        PlatformDataRequestWrapperEntity newEnt = new PlatformDataRequestWrapperEntity();
        newEnt.setPlatformName(platformNameTextField.getValue());
        newEnt.setUrl(urlTextField.getValue());
        newEnt.setAccessible(checkbox.getValue());

        this.apisService.addNewApi(newEnt);
        this.grid.setItems(apisService.getAll());
        this.grid.getDataProvider().refreshAll();
    }

    private void configureGrid(List<PlatformDataRequestWrapperEntity> all) {
        grid.addClassNames("apis-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addColumn(PlatformDataRequestWrapperEntity::getPlatformName).setHeader("Platform Name");
        grid.addColumn(PlatformDataRequestWrapperEntity::getUrl).setHeader("URL");
        grid.addColumn(PlatformDataRequestWrapperEntity::getAccessible).setHeader("Accessible");

        grid.setVisible(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addSelectionListener(event -> onGridSelect(event));

        grid.setItems(all);
    }

    private void onGridSelect(SelectionEvent<Grid<PlatformDataRequestWrapperEntity>, PlatformDataRequestWrapperEntity> event) {
        if (event.getFirstSelectedItem().isPresent()){
            this.selected = event.getFirstSelectedItem().get();
            this.platformNameTextField.setValue(selected.getPlatformName());
            this.urlTextField.setValue(selected.getUrl());
            this.checkbox.setValue(selected.getAccessible());
        }
    }
}
