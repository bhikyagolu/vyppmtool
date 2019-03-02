package com.veeryash.ppmtool.services;

import com.veeryash.ppmtool.domain.Backlog;
import com.veeryash.ppmtool.domain.Project;
import com.veeryash.ppmtool.domain.ProjectTask;
import com.veeryash.ppmtool.exceptions.ProjectNotFoundException;
import com.veeryash.ppmtool.repositories.BacklogRepository;
import com.veeryash.ppmtool.repositories.ProjectRepository;
import com.veeryash.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {



        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);

        projectTask.setBacklog(backlog);
        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;

        backlog.setPTSequence(backlogSequence);

        projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        // projectTask.getPriority() == 0 ||
        if ( projectTask.getPriority() == null || projectTask.getPriority() == 0) {
             projectTask.setPriority(3);
        }

        if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String backlogId, String username) {

        Project project = projectService.findProjectByIdentifier(backlogId, username); // findByProjectIdentifier(backlogId.toUpperCase());
        if (project == null) {
            throw new ProjectNotFoundException("Project with ID: "+backlogId.toUpperCase() +" does not exists.");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String psId, String username) {
        // make sure we are searching on the right backlog

        projectService.findProjectByIdentifier(backlogId, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(psId);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project Task "+psId+" not found.");
        }

        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("Project Task "+psId+" does not exists in project.");
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask projectTask,String backlogId, String psId, String username) {
        ProjectTask projectTask1 = findPTByProjectSequence(backlogId, psId, username);

        return projectTaskRepository.save(projectTask);
    }

    public void deleteByProjectSequence(String backlogId, String psId, String username) {
        ProjectTask projectTask = findPTByProjectSequence(backlogId, psId, username);

        /*Backlog backlog = projectTask.getBacklog();
        List<ProjectTask> projectTasks = backlog.getProjectTasks();
        projectTasks.remove(projectTask);
        backlogRepository.save(backlog);*/

        projectTaskRepository.delete(projectTask);

    }


}
