package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.Projects;
import com.bezkoder.spring.login.models.Solve;
import com.bezkoder.spring.login.models.Tasks;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.request.ProjectRequest;
import com.bezkoder.spring.login.payload.request.ProjectUserRequest;
import com.bezkoder.spring.login.payload.request.SolveRequest;
import com.bezkoder.spring.login.payload.request.TaskRequest;
import com.bezkoder.spring.login.repository.ProjectsRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(
        allowCredentials = "true",
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        maxAge = 3600)
@RestController
@RequestMapping("/api/projects")
public class ProjectsController {
    @Autowired
    ProjectsRepository projectsRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("{project_id}/addSolve")
    public ResponseEntity<?> addSolve(@PathVariable String project_id, @RequestBody SolveRequest solveRequest) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Solve> solves = project.get().getSolves();
            Solve solve = new Solve(solveRequest.getUsername(), solveRequest.getImg());
            solves.add(solve);
            project.get().setSolves(solves);
            projectsRepository.save(project.get());
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.ok(false);
    }

    @DeleteMapping("{project_id}/deleteSolve/{username}")
    public ResponseEntity<?> deleteSolve(@PathVariable String project_id, @PathVariable String username) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Solve> solves = project.get().getSolves();
            solves.removeIf(solve -> solve.getUsername().equals(username));
            project.get().setSolves(solves);
            projectsRepository.save(project.get());
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("{project_id}/decideSolve/{username}")
    public ResponseEntity<?> decideSolve(@PathVariable String project_id, @PathVariable String username, @RequestBody boolean decide) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Solve> solves = project.get().getSolves();
            Solve solve = new Solve();
            for (Solve found_solve : solves) {
                if (found_solve.getUsername().equals(username)) {
                    solve.setAccept(decide);
                    solve.setViewed(true);
                    project.get().setSolves(solves);
                    projectsRepository.save(project.get());
                    return ResponseEntity.ok(true);
                }
            }
        }
        return ResponseEntity.ok(false);
    }


    @PostMapping("/{project_id}/addUser")
    public ResponseEntity<?> addUser(@PathVariable String project_id, @RequestBody ProjectUserRequest projectUserRequest) {
        Optional<Projects> project = projectsRepository.findById(project_id);

        if (project.isPresent()) {
            List<User> users = project.get().getUsers();
            Optional<User> user = userRepository.findById(projectUserRequest.getUser_id());
            if (user.isPresent()) {
                users.add(user.get());
                project.get().setUsers(users);
                projectsRepository.save(project.get());
            } else {
                return ResponseEntity.ok(false);
            }
            return ResponseEntity.ok(true);
        } else

            return ResponseEntity.ok(false);
    }

    @GetMapping("/{project_id}/getUsers")
    public ResponseEntity<?> getUsers(@PathVariable String project_id) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent())
            return ResponseEntity.ok(project.get().getUsers());
        else
            return ResponseEntity.ok(false);
    }

    @PostMapping("/{project_id}/addTask")
    public ResponseEntity<?> addTask(@PathVariable String project_id, @RequestBody TaskRequest taskRequest) {
        Tasks task = new Tasks(taskRequest.getTitle(), taskRequest.getDescription(), taskRequest.getLink(), taskRequest.getReward());

        Optional<Projects> project = projectsRepository.findById(project_id);

        if (project.isPresent()) {
            List<Tasks> tasks = project.get().getTasks();
            tasks.add(task);
            project.get().setTasks(tasks);
            projectsRepository.save(project.get());
        }

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("{project_id}/deleteTask/{task_id}")
    public ResponseEntity<?> deteleTask(@PathVariable String project_id, @PathVariable String task_id) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Tasks> tasks = project.get().getTasks();
            tasks.removeIf(tasks1 -> tasks1.getId().equals(task_id));
            project.get().setTasks(tasks);
            projectsRepository.save(project.get());
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }


    @GetMapping("/{project_id}/getTasks")
    public ResponseEntity<?> getTasksFromProject(@PathVariable String project_id) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            return ResponseEntity.ok(project.get().getTasks());
        }
        return ResponseEntity.ok(false);
    }


    @PostMapping("/addProject")
    public ResponseEntity<?> addProjects(@RequestBody ProjectRequest projectRequest) {
        Projects project = new Projects(projectRequest.getRecurrence(), projectRequest.getUser_id(), projectRequest.getTitle(), projectRequest.getDescription(), projectRequest.getLink(), projectRequest.getTwitter(), projectRequest.getDiscord(),  projectRequest.getTelegram(), projectRequest.getWallet(), projectRequest.getCategory());
        projectsRepository.save(project);
        return ResponseEntity.ok(project);
    }


    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<?> deteleProject(@PathVariable String id) {
        projectsRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/getProject/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Projects> project = projectsRepository.findById(id);
        if (!project.isPresent()) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(project);
    }

    @GetMapping("/getProjects")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(projectsRepository.findAll());
    }

    @GetMapping("/getByUser/{user_id}")
    public ResponseEntity<?> getByUserId(@PathVariable String user_id) {
        List<Projects> projects = projectsRepository.findByUserId(user_id);

        if (projects.isEmpty()) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(projects);
    }


}
