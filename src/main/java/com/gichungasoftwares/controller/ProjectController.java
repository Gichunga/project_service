package com.gichungasoftwares.controller;

import com.gichungasoftwares.model.Project;
import com.gichungasoftwares.model.ProjectStatus;
import com.gichungasoftwares.model.UserDto;
import com.gichungasoftwares.services.ProjectService;
import com.gichungasoftwares.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    //create project
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDto user = userService.findUserByJwt(jwt);
        Project createdProject = projectService.createProject(project, user.getRole());
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    //get project by provided id
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable("projectId") Long id) throws Exception {
        Project project = projectService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    //get all projects
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(required = false) ProjectStatus projectStatus,
            @RequestHeader("Authorization") String jwt) {
        List<Project> projects = projectService.getAllProjects(projectStatus);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // update project - only admin
    @PutMapping("/{projectId}/update")
    public ResponseEntity<Project> updateProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody Project project,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto user = userService.findUserByJwt(jwt);
        Project updatedProject = projectService.updateProject(projectId, project, user.getRole());
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    // assign project to user - only admin can assign projects to users
    @PutMapping("/{projectId}/user/{userId}/assignee")
    public ResponseEntity<Project> assignProjectToUser(
            @PathVariable("projectId") Long projectId,
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto user = userService.findUserByJwt(jwt);
        return new ResponseEntity<>(projectService.assignProjectToUser(userId, projectId, user.getRole()), HttpStatus.OK);
    }

    // get projects assigned to user
    @GetMapping("/user")
    public ResponseEntity<List<Project>> getAssignedUserProjects(
            @RequestParam(required = false) ProjectStatus projectStatus,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto userDto = userService.findUserByJwt(jwt);
        List<Project> projects = projectService.assignedUserProjects(userDto.getId(), projectStatus);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // mark project as completed
    @PutMapping("/{projectId}/complete")
    public ResponseEntity<Project> completeProject(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto loggedUser = userService.findUserByJwt(jwt);
        String requesterRole = loggedUser.getRole();
        Long requesterId = loggedUser.getId();
        return new ResponseEntity<>(projectService.markProjectAsComplete(projectId, requesterId, requesterRole), HttpStatus.OK);
    }

    // delete a project
    @DeleteMapping("/{projectId}/delete")
    public ResponseEntity<?> deleteProject(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        //only admins can delete projects
        UserDto loggedUser = userService.findUserByJwt(jwt);
        projectService.deleteProject(projectId, loggedUser.getRole());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
