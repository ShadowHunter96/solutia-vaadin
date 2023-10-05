package com.example.application.marekdemo.ui;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import cz.solutia.cerberus.commons.component.ui.interfaces.SaveAction;
import cz.solutia.cerberus.marekdemo.dto.MarekDTO;
import cz.solutia.cerberus.marekdemo.entity.MarekEntity;

import java.util.List;

/**
 * Created by marek.vu on 05.10.2023.
 */
public class MarekForm extends FormLayout {
    TextField name = new TextField("Name");
    TextField description = new TextField("Description");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    BeanValidationBinder<MarekDTO>binder = new BeanValidationBinder<>(MarekDTO.class);

    private SaveAction<MarekDTO> saveAction;



    public MarekForm(List<MarekDTO>entities, SaveAction<MarekDTO> marekSaveAction){
        this.saveAction = marekSaveAction;
        addClassName("marek-form");
        binder.bindInstanceFields(this);
        binder.setBean(new MarekDTO());

        add(name,
                description,
                createButtonsLayout());


    }
    private HorizontalLayout createButtonsLayout(){
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event->validateAndSave());
//        delete.addClickListener(event-> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event-> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e->save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save,delete,close);
    }

    private void validateAndSave() {
        if(binder.isValid()){
            //fireEvent(new SaveEvent(this, binder.getBean()));
            saveAction.saveItem(binder.getBean(), null);
            binder.setBean(new MarekDTO());
        }
    }

    //events
    // Events
    // Events
    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<MarekForm> {
        private MarekDTO marekDTO;

        protected ContactFormEvent(MarekForm source, MarekDTO marekDTO) {
            super(source, false);
            this.marekDTO = marekDTO;
        }

        public MarekEntity getMarekDTO() {
            return new MarekEntity();
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(MarekForm source, MarekDTO marekDTO) {
            super(source,marekDTO);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(MarekForm source, MarekDTO marekDTO) {
            super(source, marekDTO);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(MarekForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }



    


}
