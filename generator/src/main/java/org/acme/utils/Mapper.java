package org.acme.utils;

import org.acme.dto.PanDTO;
import org.acme.entity.PanEntity;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Mapper {

    public PanDTO mapPanEntityToPanDTO(PanEntity pan) {
        PanDTO panDTO = new PanDTO();

        panDTO.setAccountNumber(pan.getAccountNumber());
        panDTO.setBin(pan.getBin());
        panDTO.setCheckDigit(pan.getCheckDigit());
        panDTO.setCreatedAt(pan.getCreatedAt());

        return panDTO;
    }

    public PanEntity mapPanDTOPanEntity(PanDTO pan) {
        PanEntity panEntity = new PanEntity();

        panEntity.setAccountNumber(pan.getAccountNumber());
        panEntity.setBin(pan.getBin());
        panEntity.setCheckDigit(pan.getCheckDigit());
        panEntity.setCreatedAt(pan.getCreatedAt());

        return panEntity;
    }
}
