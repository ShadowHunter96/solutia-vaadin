package com.example.application.marekdemo.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import cz.solutia.cerberus.mainlayout.ui.MainLayout;
import cz.solutia.cerberus.marekdemo.MarekService;
import cz.solutia.cerberus.marekdemo.dto.MarekDTO;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.PermitAll;
import java.util.Collections;
import java.util.List;

/**
 * Created by marek.vu on 05.10.2023.
 */
@Route(value = MarekView.ROUTE, layout = MainLayout.class)
@PermitAll
@Scope(value = "vaadin-ui")
@SpringComponent
public class MarekView extends VerticalLayout {
    Grid<MarekDTO> grid = new Grid<>(MarekDTO.class);
    MarekService marekService;
    MarekForm marekForm;
    TextField filterText = new TextField();


    public static final String ROUTE = "marek";

    public MarekView(MarekService marekService) {
        this.marekService = marekService;
        setSizeFull();
        configureGrid();
        configureForm();

        List<MarekDTO> taskDtos = marekService.getAllTaskDtos();
        grid.setItems(taskDtos);
        add( getToolbar(),getContent());
    }

    private void configureGrid() {
        grid.addClassName("TaskEntity1-grid");
        grid.setSizeFull();
        grid.setColumns("id","name", "description");
        grid.getColumns().forEach(col->col.setAutoWidth(true));
    }

    private void configureForm(){
        marekForm = new MarekForm(Collections.emptyList(),marekService.getSaveAction());

        marekForm.setWidth("25em");
        marekForm.addSaveListener(this::saveMarek);
        marekForm.addDeleteListener(this::deleteMarek);

    }

    private Component getContent(){
        HorizontalLayout content = new HorizontalLayout(grid,marekForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, marekForm);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    //toolbar
    private HorizontalLayout getToolbar(){
        filterText.setPlaceholder("Filter by name...");//cerberus_new_db
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e->updateList());

        Button addMarekButton = new Button("Add Marek");
        var toolBar = new HorizontalLayout(filterText, addMarekButton);
        return toolBar;

    }
    private void updateList(){
        grid.setItems(marekService.findAllMarekDtos(filterText.getValue()));
    }

    private void saveMarek(MarekForm.SaveEvent event){
        marekService.saveMarek(event.getMarekDTO());
        updateList();
    }
    private void deleteMarek(MarekForm.DeleteEvent event) {
        marekService.deleteMarek(event.getMarekDTO());
        updateList();
    }
}
    
