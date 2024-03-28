package com.gichungasoftwares.services;

import com.gichungasoftwares.model.Project;
import com.gichungasoftwares.model.ProjectStatus;
import com.gichungasoftwares.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;

    // create project
    @Override
    public Project createProject(Project project, String requesterRole) throws Exception {
        if(!requesterRole.equals(("ROLE_ADMIN"))) {
            throw new Exception("Error: only an admin can create projects");
        }
        //defaults
        project.setCreatedAt(LocalDateTime.now());
        project.setStatus(ProjectStatus.PENDING);
        //save task
        return projectRepository.save(project);
    }

    // get project by id
    @Override
    public Project getProjectById(Long id) throws Exception {
        //return project with provided id or throw an exception
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Project not found! " +id));
    }

    // get all projects
    @Override
    public List<Project> getAllProjects(ProjectStatus projectStatus) {
        // in case project status is not provided
        List<Project> allProjects = projectRepository.findAll();
        // in case status is provided - filter

        return allProjects.stream().filter(
                project -> projectStatus == null || project.getStatus().name().equalsIgnoreCase(projectStatus.toString())
        ).collect(Collectors.toList());
    }

    // update project
    @Override
    public Project updateProject(Long id, Project updatedProject, String requesterRole) throws Exception {
        // check if project exists in the database
        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Project not found with that id! " +id));
        if (existingProject == null) {
            throw new Exception("Project not found!");
        }
        if(!requesterRole.equals(("ROLE_ADMIN"))) {
            throw new Exception("Error: only an admin can update projects");
        }

        //check provided fields from user request
        if(updatedProject.getTitle() != null){
            existingProject.setTitle(updatedProject.getTitle());
        } else if (updatedProject.getDescription() != null) {
            existingProject.setDescription(updatedProject.getDescription());
        } else if (updatedProject.getImage() != null) {
            existingProject.setImage(updatedProject.getImage());
        } else if (updatedProject.getStatus() != null) {
            existingProject.setStatus(updatedProject.getStatus());
        } else if (updatedProject.getDeadline() != null) {
            existingProject.setDeadline(updatedProject.getDeadline());
        }
        return projectRepository.save(existingProject);
    }

    //assign project to user
    @Override
    public Project assignProjectToUser(Long userId, Long projectId, String requesterRole) throws Exception {
        // Check if project exists
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Error: Project is not found"));
        if( project == null ){
            throw new Exception("project does not exist");
        }
        // only admin can update projects
        if(!requesterRole.equals(("ROLE_ADMIN"))) {
            throw new Exception("Error: only an admin can update projects");
        }

        // set project status to assigned, and the assign to a user
        project.setStatus(ProjectStatus.ASSIGNED);
        project.setAssignedUserId(userId);
        return projectRepository.save(project);
    }

    // get projects assigned to a user
    @Override
    public List<Project> assignedUserProjects(Long userId, ProjectStatus projectStatus) {
        List<Project> userProjects = projectRepository.findByAssignedUserId(userId);
        // user projects by status
        System.out.println("project status -------- " + projectStatus);
        List<Project> filteredProjects = userProjects.stream().filter(
                project -> projectStatus == null || project.getStatus().name().equalsIgnoreCase(projectStatus.toString())
        ).collect(Collectors.toList());

        return filteredProjects;
    }

    // mark project as complete
    @Override
    public Project markProjectAsComplete(Long projectId, Long requesterId, String requesterRole) throws Exception {
        // check if project exists
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Error: Project is not found"));
        if( project == null ){
            throw new Exception("project does not exist");
        }

        // check for project status
        ProjectStatus projectStatus = project.getStatus();
        System.out.println("Project status: " + projectStatus);
        if(projectStatus == ProjectStatus.DONE){
            throw new Exception("Project had already been marked as complete ");
        }

        // check for admin role or project owner
        if(!requesterId.equals(project.getAssignedUserId()) || !requesterRole.equals(("ROLE_ADMIN"))){
            System.out.println("requesterId------ " +requesterId);
            System.out.println("assigned user id ------ " +project.getAssignedUserId());
            throw new RuntimeException("Error: You do not have authority to mark this project as complete or project has not been assigned!");
        }

        // set project status as complete
        project.setStatus(ProjectStatus.DONE);
        return projectRepository.save(project);
    }

    // delete project
    @Override
    public void deleteProject(Long id, String requesterRole) throws Exception {
        // check if project exists
        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Project not found with that id! " +id));
        if (existingProject == null) {
            throw new Exception("Project not found!");
        }
        // only admins can delete projects
        if( !requesterRole.equals(("ROLE_ADMIN")) ){
            System.out.println("requester role ------ " + requesterRole);
            throw new RuntimeException("Error: You do not have authority to delete this project !");
        }
        projectRepository.deleteById(id);
    }
}
