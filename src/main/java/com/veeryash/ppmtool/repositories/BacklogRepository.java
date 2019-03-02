package com.veeryash.ppmtool.repositories;

import com.veeryash.ppmtool.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {

    Backlog findByProjectIdentifier(String identifier);
    @Override
    Iterable<Backlog> findAll();
}
