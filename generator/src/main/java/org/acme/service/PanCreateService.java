package org.acme.service;

import java.util.ArrayList;
import java.util.List;

import org.acme.dto.PanDTO;
import org.acme.entity.PanEntity;
import org.acme.repository.PanRepository;
import org.acme.utils.Mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PanCreateService {

    @Inject
    private PanRepository panRepository;

    @Inject
    private Mapper mapper;

    public void save(PanDTO panDTO) {
        PanEntity panEntity = mapper.mapPanDTOPanEntity(panDTO);
        panRepository.persist(panEntity);
    }
}
