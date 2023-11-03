package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.*;
import com.bezkoder.spring.login.payload.request.*;
import com.bezkoder.spring.login.repository.ProjectsRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(allowCredentials = "true",
//        origins = "https://fastalaneapp.netlify.app/",
        origins = "http://localhost:3000", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/projects")
public class ProjectsController {
    @Autowired
    ProjectsRepository projectsRepository;
    @Autowired
    UserRepository userRepository;


    @PostMapping("{project_id}/{index_task}/addOtherSolve")
    public ResponseEntity<?> addSolve(@PathVariable String project_id, @PathVariable int index_task, @Valid @RequestBody SolveRequest solveRequest) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Object> solves = project.get().getTasks().get(index_task).getSolves();
            Solve solve = new Solve(solveRequest.getUsername(), solveRequest.getImg());
            boolean ma_doare_ficatu = false;
            for (Object sol : solves) {
                if (sol instanceof Solve) {
                    if (((Solve) sol).getUsername().equals(solveRequest.getUsername())) {
                        System.out.println("=============");
                        if (((Solve) sol).isViewed() && !((Solve) sol).isAccept()) {
                            ((Solve) sol).setImg(solveRequest.getImg());
                            project.get().getTasks().get(index_task).setSolves(solves);
                            projectsRepository.save(project.get());
                            return ResponseEntity.ok(true);
                        }
                        ma_doare_ficatu = true;
                        break;
                    }
                }
            }
            if (!ma_doare_ficatu) {
                solves.add(solve);
                project.get().getTasks().get(index_task).setSolves(solves);
                projectsRepository.save(project.get());
                return ResponseEntity.ok(true);
            }
//            if (!solves.contains(solve)) {
//                solves.add(solve);
//                project.get().getTasks().get(index_task).setSolves(solves);
//                projectsRepository.save(project.get());
//                return ResponseEntity.ok(true);
//            }
            else return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(false);
    }


    @PostMapping("{tweetId}/{project_id}/{index_task}/addAutoSolve")
    public ResponseEntity<?> addSolve(@PathVariable String tweetId, @PathVariable String project_id, @PathVariable int index_task, @Valid @RequestBody AutoSolveRequest autoSolveRequest) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Object> solves = project.get().getTasks().get(index_task).getSolves();
            AutoSolve solve = new AutoSolve(autoSolveRequest.getUsername(), autoSolveRequest.getXusername());
            boolean help_pls = false;
            if (solves.size() == 0) help_pls = true;
            System.out.println(solves.size());
            for (Object sol : solves) {
                if (sol instanceof AutoSolve) {
                    System.out.println("---------------------------------------------------------p");
                    System.out.println(((AutoSolve) sol).getUsername());
                    System.out.println(((AutoSolve) sol).getXusername());

                    System.out.println(autoSolveRequest.getUsername());
                    System.out.println(autoSolveRequest.getXusername());

//                    System.out.println();
                    System.out.println(autoSolveRequest.getXusername().equals(((AutoSolve) sol).getXusername()) && autoSolveRequest.getUsername().equals(((AutoSolve) sol).getUsername()));


                    if (((AutoSolve) sol).getUsername().equals(autoSolveRequest.getUsername()) && ((AutoSolve) sol).getXusername().equals(autoSolveRequest.getXusername())) {
                        System.out.println("((AutoSolve) sol).getUsername()");
                        System.out.println(((AutoSolve) sol).getUsername());
                        help_pls = true;
                        break;
                    }
                }
            }
            System.out.println(help_pls);

            if (help_pls) {
                System.out.println("=======================================");
                System.out.println(autoSolveRequest.getXusername());
                System.out.println(tweetId);
                try {
                    // Set timeouts for Unirest (if needed)
                    Unirest.setTimeouts(0, 0);

                    // Step 1: Get the user's ID based on the username
                    HttpResponse<String> userLookupResponse = Unirest.get(userLookupUrl + autoSolveRequest.getXusername()).header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAPwVqwEAAAAAbtXPTNe4TYMnL6n7QHLBVVDjZPo%3Dbiwuhdj2hcm6xmbZ9FnABfxQfNyVyt1YPxG5TXQcEWjYytSg9E").asString();

                    if (userLookupResponse.getStatus() != 200) {
                        return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    String userLookupJson = userLookupResponse.getBody();
                    JSONObject userLookupObject = new JSONObject(userLookupJson);
                    String userId = userLookupObject.getJSONObject("data").getString("id");
                    System.out.println("User ID: " + userId);
                    // Step 2: Get the liked tweets of the user
                    HttpResponse<String> response = Unirest.get(twitterApiUrl + userId + "/liked_tweets").header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAPwVqwEAAAAAbtXPTNe4TYMnL6n7QHLBVVDjZPo%3Dbiwuhdj2hcm6xmbZ9FnABfxQfNyVyt1YPxG5TXQcEWjYytSg9E").asString();

                    if (response.getStatus() == 200) {
                        String responseJson = response.getBody();
                        JSONObject responseObject = new JSONObject(responseJson);
                        JSONArray tweetData = responseObject.getJSONArray("data");

                        // Check if the user has liked the tweet with the specified tweetId
                        for (int i = 0; i < tweetData.length(); i++) {
                            JSONObject tweet = tweetData.getJSONObject(i);
                            String tweetIdFromResponse = tweet.getString("id");

                            if (tweetIdFromResponse.equals(tweetId)) {
                                solve.setStatus(true);
                                solves.add(solve);
                                project.get().getTasks().get(index_task).setSolves(solves);
                                projectsRepository.save(project.get());
                                System.out.println("E TOOOP");
                                return ResponseEntity.ok(true);
                            }
                        }

                        solves.add(solve);
                        project.get().getTasks().get(index_task).setSolves(solves);
                        projectsRepository.save(project.get());
                        // User has not liked the tweet with the specified tweetId
                        return ResponseEntity.ok(false);
                    } else {
                        return new ResponseEntity<>("Error retrieving liked tweets from Twitter: " + response.getStatusText(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } catch (Exception e) {

                    solves.add(solve);
                    project.get().getTasks().get(index_task).setSolves(solves);
                    projectsRepository.save(project.get());

                    return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(false);
    }

    @GetMapping("{project_id}/{index_task}/getSolves")
    public ResponseEntity<?> getSolves(@PathVariable String project_id, @PathVariable int index_task) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Object> solves = project.get().getTasks().get(index_task).getSolves();
            return ResponseEntity.ok(solves);
        }
        return ResponseEntity.ok(false);
    }

    //    @PreAuthorize()
    @DeleteMapping("{project_id}/{index_task}/deleteSolve/{username}")
    public ResponseEntity<?> deleteSolve(@PathVariable String project_id, @PathVariable int index_task, @PathVariable String username) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Object> solves = project.get().getTasks().get(index_task).getSolves();

            for (Object solve : solves) {
                if (solve instanceof Solve) {
                    if (((Solve) solve).getUsername().equals(username)) {
                        solves.remove(solve);
                    }
//                    solves.removeIf(solvev -> solvev.getUsername().equals(username));
                } else if (solve instanceof AutoSolve) {
                    if (((AutoSolve) solve).getUsername().equals(username)) {
                        solves.remove(solve);
                    }
//                    solves.removeIf(solvev -> solvev.getUsername().equals(username));
                }
            }

//            solves.removeIf(solve -> solve.getUsername().equals(username));

            project.get().getTasks().get(index_task).setSolves(solves);
            projectsRepository.save(project.get());
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("{project_id}/{index_task}/decideSolve/{username}")
    public ResponseEntity<?> decideSolve(@PathVariable String project_id, @PathVariable int index_task, @PathVariable String username, @Valid @RequestBody SolveDecideRequest solveDecideRequest) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Object> solves = project.get().getTasks().get(index_task).getSolves();
            for (Object found_solve : solves) {
                if (found_solve instanceof Solve) {
                    if (((Solve) found_solve).getUsername().equals(username)) {
                        ((Solve) found_solve).setAccept(solveDecideRequest.isDecide());
                        ((Solve) found_solve).setViewed(true);
                        project.get().getTasks().get(index_task).setSolves(solves);
                        projectsRepository.save(project.get());
                        return ResponseEntity.ok(project.get().getTasks().get(index_task).getSolves());
                    }
                } else if (found_solve instanceof AutoSolve) {
                    if (((AutoSolve) found_solve).getUsername().equals(username)) {
                        ((AutoSolve) found_solve).setStatus(true);
                        project.get().getTasks().get(index_task).setSolves(solves);
                        projectsRepository.save(project.get());
                        return ResponseEntity.ok(project.get().getTasks().get(index_task).getSolves());
                    }
                }
            }
        }
        return ResponseEntity.ok(false);
    }


    @PostMapping("/{project_id}/addUser")
    public ResponseEntity<?> addUser(@PathVariable String project_id, @Valid @RequestBody ProjectUserRequest projectUserRequest) {
        Optional<Projects> project = projectsRepository.findById(project_id);

        if (project.isPresent()) {
            List<User> users = project.get().getUsers();
            Optional<User> user = userRepository.findById(projectUserRequest.getUser_id());
            if (user.isPresent()) {
                if (!users.contains(user.get())) {
                    users.add(user.get());
                    project.get().setUsers(users);
                    projectsRepository.save(project.get());
                } else return ResponseEntity.ok(false);
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
        if (project.isPresent()) return ResponseEntity.ok(project.get().getUsers());
        else return ResponseEntity.ok(false);
    }

    @PostMapping("/{project_id}/{user_id}/addTask")
    public ResponseEntity<?> addTask(@PathVariable String project_id, @PathVariable String user_id, @Valid @RequestBody TaskRequest taskRequest) {
        Tasks task = new Tasks(taskRequest.getTitle(), taskRequest.getDescription(), taskRequest.getLink(), taskRequest.getReward(), taskRequest.getType());
        Optional<Projects> project = projectsRepository.findById(project_id);

        if (project.isPresent()) {
            if (project.get().getUser_id().equals(user_id)) {
                List<Tasks> tasks = project.get().getTasks();
                tasks.add(task);
                project.get().setTasks(tasks);
                projectsRepository.save(project.get());
            } else {
                throw new UnknownAddress("ayaye");
            }
        } else {
            throw new UnknownAddress("ayaye2");
        }

        return ResponseEntity.ok(task);

//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            UserDetails user = (UserDetails) principal;
//            Optional<User> response = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
//            if (Objects.equals(user_id, response.get().getId())) {
//
//                Tasks task = new Tasks(taskRequest.getTitle(), taskRequest.getDescription(), taskRequest.getLink(), taskRequest.getReward());
//                Optional<Projects> project = projectsRepository.findById(project_id);
//
//                if (project.isPresent()) {
//                    List<Tasks> tasks = project.get().getTasks();
//                    tasks.add(task);
//                    project.get().setTasks(tasks);
//                    projectsRepository.save(project.get());
//                }
//
//                return ResponseEntity.ok(task);
//            }
//        }
//        return  ResponseEntity.ok(false);
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
    public ResponseEntity<?> addProjects(@Valid @RequestBody ProjectRequest projectRequest) {
        Projects project = new Projects(projectRequest.getUser_id(), projectRequest.getImg(), projectRequest.getTitle(), projectRequest.getDescription(), projectRequest.getLink(), projectRequest.getTwitter(), projectRequest.getDiscord(), projectRequest.getTelegram(), projectRequest.getWallet(), projectRequest.getCategory());
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

    private final String twitterApiUrl = "https://api.twitter.com/2/users/"; // Replace with the actual Twitter API endpoint
    private final String userLookupUrl = "https://api.twitter.com/2/users/by/username/";

    @GetMapping("/check-liked-tweet/{username}/{tweetId}")
    public ResponseEntity<?> checkLikedTweet(@PathVariable String username, @PathVariable String tweetId) {
        System.out.println("=======================================");
        System.out.println(username);
        System.out.println(tweetId);
        try {
            // Set timeouts for Unirest (if needed)
            Unirest.setTimeouts(0, 0);

            // Step 1: Get the user's ID based on the username
            HttpResponse<String> userLookupResponse = Unirest.get(userLookupUrl + username).header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAPwVqwEAAAAAbtXPTNe4TYMnL6n7QHLBVVDjZPo%3Dbiwuhdj2hcm6xmbZ9FnABfxQfNyVyt1YPxG5TXQcEWjYytSg9E").asString();

            if (userLookupResponse.getStatus() != 200) {
                return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String userLookupJson = userLookupResponse.getBody();
            JSONObject userLookupObject = new JSONObject(userLookupJson);
            String userId = userLookupObject.getJSONObject("data").getString("id");
            System.out.println("User ID: " + userId);
            // Step 2: Get the liked tweets of the user
            HttpResponse<String> response = Unirest.get(twitterApiUrl + userId + "/liked_tweets").header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAPwVqwEAAAAAbtXPTNe4TYMnL6n7QHLBVVDjZPo%3Dbiwuhdj2hcm6xmbZ9FnABfxQfNyVyt1YPxG5TXQcEWjYytSg9E").asString();

            if (response.getStatus() == 200) {
                String responseJson = response.getBody();
                JSONObject responseObject = new JSONObject(responseJson);
                JSONArray tweetData = responseObject.getJSONArray("data");

                // Check if the user has liked the tweet with the specified tweetId
                for (int i = 0; i < tweetData.length(); i++) {
                    JSONObject tweet = tweetData.getJSONObject(i);
                    String tweetIdFromResponse = tweet.getString("id");

                    if (tweetIdFromResponse.equals(tweetId)) {

                        return ResponseEntity.ok(true);
                    }
                }

                // User has not liked the tweet with the specified tweetId
                return ResponseEntity.ok(false);
            } else {
                return new ResponseEntity<>("Error retrieving liked tweets from Twitter: " + response.getStatusText(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private final String FollowLookupUrl = "https://api.twitter.com/2/users/";

    @GetMapping("/check-user-subscribed/{username}/{userIdMain}/{cKey}/{cSecret}")
    public ResponseEntity<String> checkUserSubscribed(@PathVariable String username, @PathVariable String userIdMain, @PathVariable String cKey, @PathVariable String cSecret) {
        try {
            // Set timeouts for Unirest (if needed)
            Unirest.setTimeouts(0, 0);

            // Step 1: Get the user's ID based on the username
            HttpResponse<String> userLookupResponse = Unirest.get(userLookupUrl + username).header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAPwVqwEAAAAAbtXPTNe4TYMnL6n7QHLBVVDjZPo%3Dbiwuhdj2hcm6xmbZ9FnABfxQfNyVyt1YPxG5TXQcEWjYytSg9E").asString();

            if (userLookupResponse.getStatus() != 200) {
                return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String userLookupJson = userLookupResponse.getBody();
            JSONObject userLookupObject = new JSONObject(userLookupJson);
            String userId = userLookupObject.getJSONObject("data").getString("id");
            System.out.println("User ID: " + userId);
            // Step 2: Get the liked tweets of the user
            HttpResponse<String> response = Unirest.post("https://api.twitter.com/2/users/1491129346085605379/following?oauth_consumer_key=zAVN863kSaGShwVZaIEy7d81Z&oauth_token=1707052841896927232-mBpJshX8VjHrcZrHVXJYedgeLonwhz&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1699017475&oauth_nonce=GFZ6iEc5GUX&oauth_version=1.0&oauth_signature=KNCmUVl4B78%2FnAYhOyXnZXDLTlE%3D").header("Cookie", "guest_id=v1%3A169892399276147872").asString();

            if (response.getStatus() == 200) {
                String responseJson = response.getBody();
                JSONObject responseObject = new JSONObject(responseJson);
                JSONArray tweetData = responseObject.getJSONArray("data");

                // Check if the user has liked the tweet with the specified tweetId
                for (int i = 0; i < tweetData.length(); i++) {
                    JSONObject tweet = tweetData.getJSONObject(i);
                    String tweetIdFromResponse = tweet.getString("id");

                    if (tweetIdFromResponse.equals(userIdMain)) {
                        // User has liked the tweet with the specified tweetId
                        return new ResponseEntity<>("true", HttpStatus.OK);
                    }
                }

                // User has not liked the tweet with the specified tweetId
                return new ResponseEntity<>("false", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error retrieving response from Twitter: " + response.getStatusText(), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


