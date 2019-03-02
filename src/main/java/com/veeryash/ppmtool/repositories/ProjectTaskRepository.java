package com.veeryash.ppmtool.repositories;

import com.veeryash.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

    Iterable<ProjectTask> findByProjectIdentifierOrderByPriority(String backlogId);

    ProjectTask findByProjectSequence(String sequence);
}
