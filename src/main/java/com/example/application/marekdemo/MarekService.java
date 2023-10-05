package com.example.application.marekdemo;


import cz.solutia.cerberus.commons.component.ui.interfaces.SaveAction;
import cz.solutia.cerberus.commons.convertible.ConvertEntities;
import cz.solutia.cerberus.marekdemo.dto.MarekDTO;
import cz.solutia.cerberus.marekdemo.entity.MarekEntity;
import cz.solutia.cerberus.marekdemo.factory.MarekFactory;
import cz.solutia.cerberus.marekdemo.repository.MarekRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MarekService {

    private final MarekRepository marekRepository;


    public MarekService(MarekRepository taskRepository) {

        this.marekRepository = taskRepository;

    }

    public void showMe() {
        //todo sem si muzes dat metody ktere chces pustit

        List<MarekDTO> taskList = ConvertEntities.fromEntities(marekRepository.findAll(), MarekFactory::fromEntity);

        System.out.println(taskList);
    }

    // returning DTO entities formed from entities in database or repository
    public List<MarekDTO> getAllTaskDtos() {
        return ConvertEntities.fromEntities(marekRepository.findAll(), MarekFactory::fromEntity);
    }

    //StringFilter
    public List<MarekDTO>findAllMarekDtos(String stringfilter){
        if (stringfilter == null || stringfilter.isEmpty()){
            return ConvertEntities.fromEntities(marekRepository.findAll(), MarekFactory::fromEntity);
        }else {
            return ConvertEntities.fromEntities(marekRepository.search(stringfilter),MarekFactory::fromEntity);
        }

    }

    @Transactional
    public void saveMarek(MarekEntity marek){
        if (marek == null){
            System.err.println("null contact");
            throw new IllegalArgumentException("Trying to save a null MarekEntity");
        }else {
            System.out.println("Saving entity: " + marek);
        }
        marekRepository.save(marek);
    }

    public void deleteMarek(MarekEntity marek){
        if (marek == null){
            System.err.println("null contact");
        }
        marekRepository.delete(marek);
        System.out.println("Saved entity: " + marek);
    }

    public SaveAction<MarekDTO> getSaveAction() {
        return (dto, originalDto) -> {
            MarekEntity entity = new MarekEntity();
            MarekFactory.fillEntity(entity,dto);
            marekRepository.save(entity);
        };
    }






}
