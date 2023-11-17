package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.*;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.request.*;
import com.bezkoder.spring.login.repository.ProjectsRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.catalina.valves.rewrite.RewriteCond;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.ApiException;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(allowCredentials = "true",
        origins = "https://board.fastlane.buzz", allowedHeaders = "*", maxAge = 3600)
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
            } else return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(false);
    }

    @PostMapping("{project_id}/{index_task}/addSimpleAutoSolve")
    public ResponseEntity<?> addAutoSolve(@PathVariable String project_id, @PathVariable int index_task, @Valid @RequestBody AutoSolveRequest autoSolveRequest) {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Object> solves = project.get().getTasks().get(index_task).getSolves();
            AutoSolve solve = new AutoSolve(autoSolveRequest.getUsername(), autoSolveRequest.getXusername());
            boolean ma_doare_ficatu = false;
            for (Object sol : solves) {
                if (sol instanceof AutoSolve) {
                    if (((AutoSolve) sol).getUsername().equals(autoSolveRequest.getUsername())) {
                        System.out.println("=============");
                        project.get().getTasks().get(index_task).setSolves(solves);
                        projectsRepository.save(project.get());
                        return ResponseEntity.ok(true);
                    }
                    ma_doare_ficatu = true;
                    break;
                }
            }
            if (!ma_doare_ficatu) {
                solves.add(solve);
                project.get().getTasks().get(index_task).setSolves(solves);
                projectsRepository.save(project.get());
                return ResponseEntity.ok(true);
            } else return ResponseEntity.ok(false);
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
            if (solves.isEmpty()) help_pls = true;
            System.out.println(solves.size());
            for (Object sol : solves) {
                if (sol instanceof AutoSolve) {
                    if (((AutoSolve) sol).getUsername().equals(autoSolveRequest.getUsername()) && ((AutoSolve) sol).getXusername().equals(autoSolveRequest.getXusername())) {
                        System.out.println("((AutoSolve) sol).getUsername()");
                        System.out.println(((AutoSolve) sol).getUsername());
                        help_pls = true;
                        break;
                    }
                }
            }
            System.out.println(help_pls);

            if (!help_pls) {
                System.out.println("=======================================");
                System.out.println(autoSolveRequest.getXusername());
                System.out.println(tweetId);

                try {
                    // Set timeouts for Unirest (if needed)
                    Unirest.setTimeouts(0, 0);

                    // Step 1: Get the user's ID based on the username
                    HttpResponse<String> userLookupResponse = Unirest.get(userLookupUrl + autoSolveRequest.getXusername())
                            .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
                            .asString();

                    if (userLookupResponse.getStatus() != 200) {
                        return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    String userLookupJson = userLookupResponse.getBody();
                    JSONObject userLookupObject = new JSONObject(userLookupJson);
                    String userId = userLookupObject.getJSONObject("data").getString("id");
                    System.out.println("User ID: " + userId);
                    // Step 2: Get the liked tweets of the user
                    HttpResponse<String> response = Unirest.get(twitterApiUrl + userId + "/liked_tweets")
                            .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
                            .asString();

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
//                try {
//                    // Set timeouts for Unirest (if needed)
//                    Unirest.setTimeouts(0, 0);
//
//                    // Step 1: Get the user's ID based on the username
//                    HttpResponse<String> userLookupResponse = Unirest.get(userLookupUrl + autoSolveRequest.getXusername()).header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAPwVqwEAAAAAbtXPTNe4TYMnL6n7QHLBVVDjZPo%3Dbiwuhdj2hcm6xmbZ9FnABfxQfNyVyt1YPxG5TXQcEWjYytSg9E").asString();
//
//                    if (userLookupResponse.getStatus() != 200) {
//                        return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
//                    }
//
//                    String userLookupJson = userLookupResponse.getBody();
//                    JSONObject userLookupObject = new JSONObject(userLookupJson);
//                    String userId = userLookupObject.getJSONObject("data").getString("id");
//                    System.out.println("User ID: " + userId);
//                    // Step 2: Get the liked tweets of the user
//                    HttpResponse<String> response = Unirest.get(twitterApiUrl + userId + "/liked_tweets").header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAPwVqwEAAAAAbtXPTNe4TYMnL6n7QHLBVVDjZPo%3Dbiwuhdj2hcm6xmbZ9FnABfxQfNyVyt1YPxG5TXQcEWjYytSg9E").asString();
//
//                    if (response.getStatus() == 200) {
//                        String responseJson = response.getBody();
//                        JSONObject responseObject = new JSONObject(responseJson);
//                        JSONArray tweetData = responseObject.getJSONArray("data");
//
//                        // Check if the user has liked the tweet with the specified tweetId
//                        for (int i = 0; i < tweetData.length(); i++) {
//                            JSONObject tweet = tweetData.getJSONObject(i);
//                            String tweetIdFromResponse = tweet.getString("id");
//
//                            if (tweetIdFromResponse.equals(tweetId)) {
//                                solve.setStatus(true);
//                                solves.add(solve);
//                                project.get().getTasks().get(index_task).setSolves(solves);
//                                projectsRepository.save(project.get());
//                                System.out.println("E TOOOP");
//                                return ResponseEntity.ok(true);
//                            }
//                        }
//
//                        solves.add(solve);
//                        project.get().getTasks().get(index_task).setSolves(solves);
//                        projectsRepository.save(project.get());
//                        // User has not liked the tweet with the specified tweetId
//                        return ResponseEntity.ok(false);
//                    } else {
//                        return new ResponseEntity<>("Error retrieving liked tweets from Twitter: " + response.getStatusText(), HttpStatus.INTERNAL_SERVER_ERROR);
//                    }
//                } catch (Exception e) {
//
//                    solves.add(solve);
//                    project.get().getTasks().get(index_task).setSolves(solves);
//                    projectsRepository.save(project.get());
//
//                    return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//                }
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
                } else if (solve instanceof AutoSolve) {
                    if (((AutoSolve) solve).getUsername().equals(username)) {
                        solves.remove(solve);
                    }
                }
            }
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
                        ((AutoSolve) found_solve).setStatus(solveDecideRequest.isDecide());
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

    //===============================================================================================================

    private final String twitterApiUrl = "https://api.twitter.com/2/users/"; // Replace with the actual Twitter API endpoint
    private final String userLookupUrl = "https://api.twitter.com/2/users/by/username/";

    //DONE
    @GetMapping("/check-liked-tweet/{username}/{tweetId}")
    public ResponseEntity<String> checkLikedTweet(
            @PathVariable String username,
            @PathVariable String tweetId
    ) {
        try {
            // Set timeouts for Unirest (if needed)
            Unirest.setTimeouts(0, 0);

            // Step 1: Get the user's ID based on the username
            HttpResponse<String> userLookupResponse = Unirest.get(userLookupUrl + username)
                    .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
                    .asString();

            if (userLookupResponse.getStatus() != 200) {
                return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String userLookupJson = userLookupResponse.getBody();
            JSONObject userLookupObject = new JSONObject(userLookupJson);
            String userId = userLookupObject.getJSONObject("data").getString("id");
            System.out.println("User ID: " + userId);
            // Step 2: Get the liked tweets of the user
            HttpResponse<String> response = Unirest.get(twitterApiUrl + userId + "/liked_tweets")
                    .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
                    .asString();

            if (response.getStatus() == 200) {
                String responseJson = response.getBody();
                JSONObject responseObject = new JSONObject(responseJson);
                JSONArray tweetData = responseObject.getJSONArray("data");

                // Check if the user has liked the tweet with the specified tweetId
                for (int i = 0; i < tweetData.length(); i++) {
                    JSONObject tweet = tweetData.getJSONObject(i);
                    String tweetIdFromResponse = tweet.getString("id");

                    if (tweetIdFromResponse.equals(tweetId)) {
                        // User has liked the tweet with the specified tweetId
                        return new ResponseEntity<>("true", HttpStatus.OK);
                    }
                }

                // User has not liked the tweet with the specified tweetId
                return new ResponseEntity<>("false", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error retrieving liked tweets from Twitter: " + response.getStatusText(), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/check-user-subscribed/{username}")
//    public ResponseEntity<String> checkUserSubscribed(@PathVariable String username) {
//        String accountIdToCheck = "1707052841896927232"; // Account ID to check if the user follows
//
//        try {
//            // Step 1: Get Twitter user ID by username
//            HttpResponse<String> userLookupResponse = Unirest.get(userLookupUrl + username)
//                    .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
//                    .asString();
//
//            if (userLookupResponse.getStatus() != 200) {
//                System.out.println("Error response body: " + userLookupResponse.getBody());
//                return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//
//            String userLookupJson = userLookupResponse.getBody();
//            JSONObject userLookupObject = new JSONObject(userLookupJson);
//            String userId = userLookupObject.getJSONObject("data").getString("id");
//
//            // Step 2: Check if the user with userId follows the specified account
////            HttpResponse<String> followCheckResponse = Unirest.get("https://api.twitter.com/2/users/" + userId + "/following/" + accountIdToCheck)
////                    .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
////                    .asString();
//            HttpResponse<String> followCheckResponse = Unirest.get("https://api.twitter.com/2/users/" + accountIdToCheck + "/followers")
//                    .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
//                    .queryString("user.fields", "id") // Include this line to retrieve user IDs in the response
//                    .asString();
//
//
//
//            if (followCheckResponse.getStatus() == 200) {
//                System.out.println("User follows the account");
//                return new ResponseEntity<>("true", HttpStatus.OK);
//            } else if (followCheckResponse.getStatus() == 404) {
//                System.out.println("User does not follow the account");
//                return new ResponseEntity<>("false", HttpStatus.OK);
//            } else {
//                System.out.println("Error response body: " + followCheckResponse.getBody());
//                return new ResponseEntity<>("Error checking user subscription", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @GetMapping("/check-user-subscribed/{username}")
//    public ResponseEntity<String> checkUserSubscribed(
//            @PathVariable String username
//    ) throws UnirestException, com.twitter.clientlib.ApiException {
//        String id = "1707052841896927232";
//        // Instantiate library client
//        TwitterApi apiInstance = new TwitterApi();
//        System.out.println(username);
//
//        TwitterCredentialsBearer credentials = new TwitterCredentialsBearer("AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC");
//        System.out.println(credentials.getBearerToken());
//        System.out.println(credentials.isBearerToken());
//        // Pass credentials to library client
//        apiInstance.setTwitterCredentials(credentials);
//
//        HttpResponse<String> userLookupResponse = Unirest.get(userLookupUrl + username)
//                .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
//                .asString();
//
//        if (userLookupResponse.getStatus() != 200) {
//            return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        String userLookupJson = userLookupResponse.getBody();
//        JSONObject userLookupObject = new JSONObject(userLookupJson);
//        String userId = userLookupObject.getJSONObject("data").getString("id");
//        System.out.println(userId);
//        try {
////            UsersFollowingLookupResponse result2 = apiInstance.users().userIdF
//            SingleUserLookupResponse result = apiInstance.users().findUserById(userId, null, null, null);
//            System.out.println(result.getData());
//            return new ResponseEntity<>("true", HttpStatus.OK);
//        } catch (ApiException e) {
//            System.out.println("2222222222222222222222222222222222222222");
//            System.err.println("Exception when calling UsersApi#usersIdFollowing");
//            e.printStackTrace();
//            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (com.twitter.clientlib.ApiException e) {
//            System.out.println("0000000000000000000000000000000000");
//            throw new RuntimeException(e);
//        }
//    }


    //
//    @GetMapping("/oauth2/callback/twitter")
//    public void getTwitter(
//            @RequestParam("oauth_verifier") String oauth_verifier,
//            @RequestParam("oauth_token") String oauth_token,
//            HttpServletResponse responses
//    ) {
//        try {
//            Date expdate = new Date();
//            expdate.setTime(expdate.getTime() + (3600 * 1000));
//            DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US);
//            df.setTimeZone(TimeZone.getTimeZone("GMT"));
//            String cookieExpire = df.format(expdate);
//
//            Unirest.setTimeouts(0, 0);
//            HttpResponse<String> response = Unirest.post("https://api.twitter.com/oauth/access_token")
//                    .header("Cookie", "guest_id=v1%3A169892399276147872")
//                    .field("oauth_token", oauth_token)
//                    .field("oauth_consumer_key", "qVeFo8ZPkQEBDk6nfuHqo2tva")
//                    .field("oauth_verifier", oauth_verifier)
//                    .field("expires", cookieExpire)
//                    .asString();
//
//            String jsonData = response.getBody();
////            System.out.println(oauth_token + oauth_verifier);
//            System.out.println("================================================================================================================");
//            System.out.println(jsonData);
//            responses.sendRedirect("https://board.fastlane.buzz&json1=");
////            return new ResponseEntity<>(jsonData, HttpStatus.OK);
//        } catch (TypeMismatchException e) {
//            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooo");
////            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (UnirestException e) {
//            System.out.println("111111111111111111111111111");
////            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @GetMapping("/oauth2/authorize/normal/twitter")
//    public void twitterOauthLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        try {
//            TwitterConnectionFactory connectionFactory =
//                    new TwitterConnectionFactory("qVeFo8ZPkQEBDk6nfuHqo2tva", "X5oTAUxTBOZeVu9fjL0ZR4tbwXup6MbcIjSZreOzWXDtAiOuID");
//            OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
//
//            // Specify your callback URL here
//            String callbackUrl = "http://localhost:8080/api/projects/oauth2/callback/twitter"; // Replace with your actual callback URL
//
//            MultiValueMap<String, String> additionalParameters = new LinkedMultiValueMap<>();
//            additionalParameters.add("oauth_callback", callbackUrl);
//
//            OAuthToken requestToken = oauthOperations.fetchRequestToken(callbackUrl, additionalParameters);
//            String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
//            response.sendRedirect(authorizeUrl);
//        } catch (TypeMismatchException e) {
//            // Handle TypeMismatchException here
//            System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
//            e.printStackTrace(); // You may want to log the exception or handle it appropriately
//        }
//    }

    private String url_front;

    @GetMapping("/oauth2/callback/twitter")
    public void getTwitter(
            @RequestParam("oauth_verifier") String oauth_verifier,
            @RequestParam("oauth_token") String oauth_token,
            HttpServletResponse responses
    ) {
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("https://api.twitter.com/oauth/access_token")
                    .header("Cookie", "guest_id=v1%3A169892399276147872")
                    .field("oauth_token", oauth_token)
                    .field("oauth_consumer_key", "qVeFo8ZPkQEBDk6nfuHqo2tva")
                    .field("oauth_verifier", oauth_verifier)
                    .asString();

            String jsonData = response.getBody();
            System.out.println("================================================================================================================");
            System.out.println(jsonData);
            responses.sendRedirect("https://board.fastlane.buzz/" + url_front + "?" + jsonData);
        } catch (TypeMismatchException e) {
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooo");
        } catch (UnirestException e) {
            System.out.println("111111111111111111111111111");
        } catch (IOException e) {
            System.out.println("sa doks oaskd oas doksa d o;asod soa doasod sdk a sd  saoo");
            throw new RuntimeException(e);
        }
//        return ResponseEntity.ok("none");
    }


    @GetMapping("/oauth2/authorize/normal/twitter/{url1}/{url2}")
    public ResponseEntity<?> twitterOauthLogin(@PathVariable String url1, @PathVariable String url2, HttpServletRequest request, HttpServletResponse response) throws IOException {
        url_front = url1 + "/" + url2;
        try {
            TwitterConnectionFactory connectionFactory =
                    new TwitterConnectionFactory("qVeFo8ZPkQEBDk6nfuHqo2tva", "X5oTAUxTBOZeVu9fjL0ZR4tbwXup6MbcIjSZreOzWXDtAiOuID");
            OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();

            // Specify your callback URL here
            String callbackUrl = "http://localhost:8080/api/projects/oauth2/callback/twitter"; // Replace with your actual callback URL

            MultiValueMap<String, String> additionalParameters = new LinkedMultiValueMap<>();
            additionalParameters.add("oauth_callback", callbackUrl);

            OAuthToken requestToken = oauthOperations.fetchRequestToken(callbackUrl, additionalParameters);
//            String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
//            response.sendRedirect(authorizeUrl);

            return ResponseEntity.ok(oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE));
        } catch (TypeMismatchException e) {
            // Handle TypeMismatchException here
            System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
            e.printStackTrace(); // You may want to log the exception or handle it appropriately

            return ResponseEntity.ok("no");
        }
    }


//    @GetMapping("/twitter-api-request/{accessToken}/{mainUserId}/{userToCheck}")
//    public ResponseEntity<String> makeTwitterApiRequest(
//            @PathVariable String accessToken,
//            @PathVariable String mainUserId,
//            @PathVariable String userToCheck
//    ) {
//        Unirest.setTimeouts(0, 0);
//        try {
//            // Step 1: Get the user's ID based on the username
//            HttpResponse<String> userLookupResponse = Unirest.get("https://api.twitter.com/2/users/by/username/" + userToCheck)
//                    .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
//                    .asString();
//
//            if (userLookupResponse.getStatus() != 200) {
//                return new ResponseEntity<>("Error retrieving user information", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//
//            String userLookupJson = userLookupResponse.getBody();
//            JSONObject userLookupObject = new JSONObject(userLookupJson);
//            String userId = userLookupObject.getJSONObject("data").getString("id");
//
//            HttpResponse<String> response = Unirest.post("https://api.twitter.com/2/users/" + mainUserId + "/following")
//                    .header("Content-Type", "application/json")
//                    .header("Authorization", "OAuth oauth_consumer_key=\"qVeFo8ZPkQEBDk6nfuHqo2tva\",oauth_token=\"" + accessToken + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1699894506\",oauth_nonce=\"15qODNoU2oI\",oauth_version=\"1.0\",oauth_signature=\"pi%2F2yczDZ5jt6WkUfBCKUqHCQlc%3D\"")
//                    .header("Cookie", "guest_id=v1%3A169892399276147872")
//                    .body("{\r\n    \"target_user_id\": \"" + userId + "\"\r\n}")
//                    .asString();
//            String responseJson = response.getBody();
//            JSONObject responseObject = new JSONObject(responseJson);
//            System.out.println(responseObject);
//            JSONObject dataObject = responseObject.getJSONObject("data");
//
//            // Extract the "following" field from the "data" object
//            boolean isFollowing = dataObject.getBoolean("following");
//
//            if (isFollowing) {
//                return new ResponseEntity<>("true", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("false", HttpStatus.OK);
//            }
//        } catch (UnirestException e) {
//            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/check-user-subscribed/{accessToken}/{accessSecret}/{userToCheck}/{username}/{project_id}/{index_task}")
    public ResponseEntity<String> makeTwitterApiRequest(
            @PathVariable String accessToken,
            @PathVariable String accessSecret,
            @PathVariable String username,
            @PathVariable String project_id,
            @PathVariable int index_task,
            @PathVariable String userToCheck
    ) throws IOException {
        Optional<Projects> project = projectsRepository.findById(project_id);
        if (project.isPresent()) {
            List<Object> solves = project.get().getTasks().get(index_task).getSolves();
            AutoSolve autoSolve = new AutoSolve(username, userToCheck);
            boolean help_pls = false;
            int index_solve = 0;
//            if (solves.isEmpty()) help_pls = true;
            System.out.println("size: " + solves.size());
            for (int i = 0; i < solves.size(); i++) {
                AutoSolve sol = (AutoSolve) solves.get(i);
                System.out.println(i);
                if (sol.getUsername().equals(username) && sol.getXusername().equals(userToCheck)) {
                    System.out.println("((AutoSolve) sol).getUsername()");
                    System.out.println(sol.getUsername());
                    help_pls = true;
                    index_solve = i;
                    break;
                }
            }

            System.out.println(help_pls);

//            if (!help_pls) {
            String consumerKey = "qVeFo8ZPkQEBDk6nfuHqo2tva";
            String consumerSecret = "X5oTAUxTBOZeVu9fjL0ZR4tbwXup6MbcIjSZreOzWXDtAiOuID";

            userToCheck = this.makeTwitterApiRequest(userToCheck);
            String fastlane_id = this.makeTwitterApiRequest("fstlaneapp");

            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
            consumer.setTokenWithSecret(accessToken, accessSecret);

            // You need to set the timestamp and nonce here
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
            String nonce = UUID.randomUUID().toString();

            // Create the OAuth parameters
            String oAuthParameters = "oauth_signature_method=HMAC-SHA1," +
                    "oauth_timestamp=" + timestamp + "," +
                    "oauth_nonce=" + nonce + "," +
                    "oauth_version=1.0";

            HttpPost request = new HttpPost("https://api.twitter.com/2/users/" + userToCheck + "/following");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Cookie", "guest_id=v1%3A169892399276147872");

            // Set OAuth parameters in the Authorization header
            request.setHeader("Authorization", "OAuth " + oAuthParameters);

            // Set the JSON body
            StringEntity body = new StringEntity("{\"target_user_id\": \"" + fastlane_id + "\"}");
            request.setEntity(body);

            // Sign the request
            try {
                consumer.sign(request);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error signing the request", HttpStatus.OK);
            }

            // Send the request
            HttpClient httpClient = new DefaultHttpClient();
            org.apache.http.HttpResponse response = httpClient.execute(request);

            // Handle the response
            int statusCode = response.getStatusLine().getStatusCode();

            String responseBody = EntityUtils.toString(response.getEntity());

            JSONObject responseObject = new JSONObject(responseBody);
            System.out.println(responseObject);

            if (responseObject.has("data")) {
                JSONObject dataObject = responseObject.getJSONObject("data");

                // Extract the "following" field from the "data" object
                boolean isFollowing = dataObject.getBoolean("following");

                if (isFollowing) {
                    System.out.println("e la follow");
                    if (help_pls) {
                        System.out.println("exista deja");
                        project.get().getTasks().get(index_task).getSolves().remove(index_solve);
                    }
                    autoSolve.setStatus(true);
                    solves.add(autoSolve);
                    project.get().getTasks().get(index_task).setSolves(solves);
                    projectsRepository.save(project.get());

                    return new ResponseEntity<>("true", HttpStatus.OK);
                } else {
                    System.out.println("nu e la follow");
                    if (help_pls) {
                        System.out.println("exista deja2");
                        project.get().getTasks().get(index_task).getSolves().remove(index_solve);
                    }
                    autoSolve.setStatus(false);
                    solves.add(autoSolve);
                    project.get().getTasks().get(index_task).setSolves(solves);
                    projectsRepository.save(project.get());
                    return new ResponseEntity<>("false", HttpStatus.OK);
                }
            } else {
                System.out.println("ayaye");
                return ResponseEntity.ok("false");
            }
        }
        return ResponseEntity.ok("false");
    }


    public String makeTwitterApiRequest(
            String userToCheck
    ) {
        Unirest.setTimeouts(0, 0);
        try {
            // Step 1: Get the user's ID based on the username
            HttpResponse<String> userLookupResponse = Unirest.get("https://api.twitter.com/2/users/by/username/" + userToCheck)
                    .header("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAOqhqwEAAAAAG3x%2FSp7T4NrIlCm8RTo789uLNzw%3D3Z1e3Nfc3cnXjjUxiBY9fLGZ3Go2jPHuavfeXeU171IlLYFfEC")
                    .asString();

            if (userLookupResponse.getStatus() != 200) {
                return "";
            }

            String userLookupJson = userLookupResponse.getBody();
            JSONObject userLookupObject = new JSONObject(userLookupJson);
            String userId = userLookupObject.getJSONObject("data").getString("id");
            return userId;
        } catch (UnirestException e) {
            return "";
        }
    }
}


