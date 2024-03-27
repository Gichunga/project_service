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
    Project updateProject(Long id, Project updatedProject, Long userId) throws Exception;
    Project assignToUser(Long userId, Long projectId) throws Exception;
    List<Project> assignedUserProjects(Long userId, ProjectStatus projectStatus);
    Project completeProject(Long projectId) throws Exception;
    void deleteProject(Long id) throws Exception;

}
