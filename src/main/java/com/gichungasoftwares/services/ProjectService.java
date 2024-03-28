package com.gichungasoftwares.services;

import com.gichungasoftwares.model.Project;
import com.gichungasoftwares.model.ProjectStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProjectService {
    Project createProject(Project project, String requesterRole) throws Exception;
    Project getProjectById(Long id) throws Exception;
    List<Project> getAllProjects(ProjectStatus projectStatus);
    Project updateProject(Long id, Project updatedProject, String requesterRole) throws Exception;
    Project assignProjectToUser(Long userId, Long projectId, String requesterRole) throws Exception;
    List<Project> assignedUserProjects(Long userId, ProjectStatus projectStatus);
    Project markProjectAsComplete(Long projectId, Long requesterId, String requesterRole) throws Exception;
    void deleteProject(Long id, String requesterRole) throws Exception;

}
