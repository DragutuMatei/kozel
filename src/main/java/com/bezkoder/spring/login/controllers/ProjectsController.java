package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.*;
import com.bezkoder.spring.login.payload.request.*;
import com.bezkoder.spring.login.repository.ProjectsRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.UsersFollowingLookupResponse;
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
import java.util.Calendar;
import java.util.Date;
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
                    System.out.println("---------------------------------------------------------p");
                    System.out.println(((AutoSolve) sol).getUsername());
                    System.out.println(((AutoSolve) sol).getXusername());

                    System.out.println(autoSolveRequest.getUsername());
                    System.out.println(autoSolveRequest.getXusername());

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


    @GetMapping("/check-user-subscribed/{username}")
    public ResponseEntity<String> checkUserSubscribed(
            @PathVariable String username
    ) {
        String id = "2244994945";
        // Instantiate library client
        TwitterApi apiInstance = new TwitterApi();

        TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(System.getenv("BEARER_TOKEN"));

        // Pass credentials to library client
        apiInstance.setTwitterCredentials(credentials);
        try {
            UsersFollowingLookupResponse result = apiInstance.users().usersIdFollowing(id, null, null);
            System.out.println(result);
            return new ResponseEntity<>("true", HttpStatus.OK);
        } catch (ApiException e) {
            System.err.println("Exception when calling UsersApi#usersIdFollowing");
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (com.twitter.clientlib.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/oauth2/callback/twitter")
    public ResponseEntity<String> getTwitter(
            @RequestParam("oauth_verifier") String oauth_verifier,
            @RequestParam("oauth_token") String oauth_token
    ) {
        try {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("https://api.twitter.com/oauth/access_token")
                    .header("Cookie", "guest_id=v1%3A169892399276147872")
                    .field("oauth_token", oauth_token)
                    .field("oauth_consumer_key", "qVeFo8ZPkQEBDk6nfuHqo2tva")
                    .field("oauth_verifier", oauth_verifier)
//                    .field("expires", strDate)
                    .asString();
            String jsonData = response.getBody();
            System.out.println(oauth_token + oauth_verifier);
            System.out.println("================================================================================================================");
            System.out.println(jsonData);
            return new ResponseEntity<>(jsonData, HttpStatus.OK);
        } catch (TypeMismatchException e) {
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooo");
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnirestException e) {
            System.out.println("111111111111111111111111111");
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/oauth2/authorize/normal/twitter")
    public void twitterOauthLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            TwitterConnectionFactory connectionFactory =
                    new TwitterConnectionFactory("qVeFo8ZPkQEBDk6nfuHqo2tva", "X5oTAUxTBOZeVu9fjL0ZR4tbwXup6MbcIjSZreOzWXDtAiOuID");
            OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();

            // Specify your callback URL here
            String callbackUrl = "http://localhost:8080/api/projects/oauth2/callback/twitter"; // Replace with your actual callback URL

            MultiValueMap<String, String> additionalParameters = new LinkedMultiValueMap<>();
            additionalParameters.add("oauth_callback", callbackUrl);

            OAuthToken requestToken = oauthOperations.fetchRequestToken(callbackUrl, additionalParameters);
            String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
            response.sendRedirect(authorizeUrl);
        } catch (TypeMismatchException e) {
            // Handle TypeMismatchException here
            System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
            e.printStackTrace(); // You may want to log the exception or handle it appropriately
        }
    }


}


