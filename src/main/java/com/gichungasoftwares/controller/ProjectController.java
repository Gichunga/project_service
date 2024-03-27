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
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") Long id) throws Exception {
        Project project = projectService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    //get all projects
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(required = false) ProjectStatus projectStatus) {
        List<Project> projects = projectService.getAllProjects(projectStatus);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // update task
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody Project project,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto user = userService.findUserByJwt(jwt);
        return new ResponseEntity<>(projectService.updateProject(projectId, project, user.getId()), HttpStatus.OK);
    }

    // assign project to user
    @PutMapping("/{projectId}/user/{userId}/assignee")
    public ResponseEntity<Project> assignProjectToUser(
            @PathVariable("projectId") Long projectId,
            @PathVariable("userId") Long userId
    ) throws Exception {
        return new ResponseEntity<>(projectService.assignProjectToUser(userId, projectId), HttpStatus.OK);
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
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> completeProject(
            @PathVariable("projectId") Long projectId
    ) throws Exception {
        return new ResponseEntity<>(projectService.markProjectAsComplete(projectId), HttpStatus.OK);
    }

    // delete a project
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(
            @PathVariable("projectId") Long projectId
    ) throws Exception {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}