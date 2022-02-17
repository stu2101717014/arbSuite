package com.example.ui.layouts.names.similarities;

import com.example.ui.entities.jpa.NamesSimilarities;
import com.example.ui.layouts.MainLayout;
import com.example.ui.security.utils.SecuredByRole;
import com.example.ui.services.NamesSimilaritiesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@SecuredByRole("ROLE_Admin")
@Route(value="namessimilarities", layout = MainLayout.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NamesSimilaritiesView extends VerticalLayout {

    private final Grid<NamesSimilarities> grid = new Grid<>(NamesSimilarities.class, true);

    private NamesSimilaritiesService namesSimilaritiesService;

    private NamesSimilaritiesFormView namesSimilaritiesFormView;

    private NamesSimilarities selected;


    public NamesSimilaritiesView(@Autowired NamesSimilaritiesService namesSimilaritiesService) {
        this.namesSimilaritiesService = namesSimilaritiesService;

        configureGrid(namesSimilaritiesService);
        this.namesSimilaritiesFormView = new NamesSimilaritiesFormView(namesSimilaritiesService, grid);

        FlexLayout flexLayout = this.configureContent(this.namesSimilaritiesFormView);

        Button showForm = new Button("Show Form");
        showForm.addClickListener(e -> {
            namesSimilaritiesFormView.setVisible(true);
            namesSimilaritiesFormView.setNamesSimilarities(this.selected);
        });

        addAndExpand(new HorizontalLayout(showForm), flexLayout);
    }

    private FlexLayout configureContent(NamesSimilaritiesFormView namesSimilaritiesFormView) {

        namesSimilaritiesFormView.setWidth("6em");
        namesSimilaritiesFormView.setVisible(false);

        FlexLayout content = new FlexLayout(grid, namesSimilaritiesFormView);
        content.setFlexGrow(4, grid);
        content.setFlexGrow(1, namesSimilaritiesFormView);
        content.setFlexShrink(0, namesSimilaritiesFormView);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();
        return content;
    }

    private void configureGrid(NamesSimilaritiesService namesSimilaritiesService) {
        grid.setItems(namesSimilaritiesService.getAll());
        grid.addSelectionListener(this::onNameSimilaritiesSelected);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setHeightFull();
    }

    private void onNameSimilaritiesSelected(SelectionEvent<Grid<NamesSimilarities>, NamesSimilarities> event) {
        if (event.getFirstSelectedItem().isPresent()){
            NamesSimilarities firstSelectedItem = event.getFirstSelectedItem().get();

            this.namesSimilaritiesFormView.setNamesSimilarities(firstSelectedItem);

            selected = firstSelectedItem;
        }
    }
}
