package org.acme.repository;

import org.acme.entity.PanEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PanRepository implements PanacheRepository<PanEntity> {

}
