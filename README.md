Simple application written in Java using Spring framework which fetches and  lists all GitHub repositories for a given user, excluding forked repositories, and including repository details like branch names and last commit SHA. 
Also, if provided login doesn't exists, exception handler will send an error code and message.

You can use one of the following api (depends on device you are using) to run the application:
```
curl -H "Accept: application/json" http://localhost:8080/api/repos/{username}
```
OR
```
Invoke-WebRequest -Uri http://localhost:8080/api/repos/TomashKarpei -Headers @{"Accept" = "application/json"}
```

If user exists, data will be displayed like written below:
```
[
    {
        "userLogin": "TomashKarpei",
        "repository": [
            {
                "repositoryName": "AlgorithmsForFunctionOptimization",
                "branches": [
                    {
                        "branchName": "main",
                        "lastCommSHA": "8bd425e2c1b9cb0cb7dad0a0f2b127e4a8d96c5d"
                    }
                ]
            }
       
            {
                "repositoryName": "BlurFilter",
                "branches": [
                    {
                        "branchName": "main",
                        "lastCommSHA": "0a0f869631486e70ac1f1957b7801ec0a45b9c0a"
                    }
                ]
            }

        ...
        
       ]
    }
]
```
If user doesn't exists, you will see the following:
```
{
    "status": 404,
    "message": "User not found."
}
```
This part of the code is a method within a controller 'RepoListerController'. It is designed to handle GET requests :
```
 @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<List<UsersRepos>> findAll(@PathVariable String username) {
        try {
            List<UsersRepos> repos = repoListerService.findAll(username);
            return ResponseEntity.ok(repos);
        } catch (GlobalControllerExceptionHandler exception) {
            throw exception;
        }
    }
```

This part of the 'findAll' method in a service 'RepoListerService' is responsible for making an GET request to the GitHub API to retrieve a list of repositories associated with a specific user:
```
String url = String.format("https://api.github.com/users/%s/repos", username);
ParameterizedTypeReference<List<Map<String, Object>>> responseType = new ParameterizedTypeReference<>() {};
ResponseEntity<List<Map<String, Object>>> response = rest.exchange(url, HttpMethod.GET, null, responseType);
```

Next part of 'finAll' processes the list of repositories, filters out forked repositories and retrieves the branches for each non-forked repository:
```
 List<GHRepository> ghRepositories = repos.stream()
                    .filter(repo -> !Boolean.TRUE.equals(repo.get("fork")))
                    .map(repo -> {
                        String repoName = (String) repo.get("name");
                        String branchesUrl = String.format("https://api.github.com/repos/%s/%s/branches", username, repoName);
                        List<Map<String, Object>> branches = rest.exchange(branchesUrl, HttpMethod.GET, null, responseType).getBody();

                        List<Branch> branchList = branches.stream()
                                .map(branch -> {
                                    String branchName = (String) branch.get("name");
                                    String lastCommitSha = (String) ((Map<String, Object>) branch.get("commit")).get("sha");
                                    return new Branch(branchName, lastCommitSha);
                                })
                                .collect(Collectors.toList());
                        return new GHRepository(repoName, branchList);
                    })
                    .collect(Collectors.toList());

            return List.of(new UsersRepos(username, ghRepositories));
```

Method 'handleUserNotFoundException' handles the exception if provided username doesn't exist:
```
@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "message", ex.getMessage()
        ), HttpStatus.NOT_FOUND);
    }
```
Important to mention that GitHub has a rate limit and allows to call it only 10 times in 1 hour. If you want to increase limit, you can modify application properties to include your GitHub token which will allow you to make more pull requests.
