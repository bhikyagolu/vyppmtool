package com.veeryash.ppmtool.services;

import com.veeryash.ppmtool.domain.Backlog;
import com.veeryash.ppmtool.domain.Project;
import com.veeryash.ppmtool.domain.User;
import com.veeryash.ppmtool.exceptions.ProjectIdException;
import com.veeryash.ppmtool.exceptions.ProjectNotFoundException;
import com.veeryash.ppmtool.repositories.BacklogRepository;
import com.veeryash.ppmtool.repositories.ProjectRepository;
import com.veeryash.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdate(Project project, String username) {
        try {

            if (project.getId() != null) {
                findProjectByIdentifier(project.getProjectIdentifier(), username);
            }
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);

        } catch (Exception e) {
            throw new ProjectIdException("Porject ID "+project.getProjectIdentifier().toUpperCase() + " already exists.");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID "+projectId.toUpperCase() +" does not exists");
        }

        if(!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in our account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {

        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }
}
