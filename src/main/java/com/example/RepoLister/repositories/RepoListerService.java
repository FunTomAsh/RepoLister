package com.example.RepoLister.repositories;

import jakarta.annotation.PostConstruct;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepoListerService {

    private List<UsersRepos> urepos = new ArrayList<>();
    private final RestTemplate rest;
    private static final String USER_REPOS_URL = "https://api.github.com/users/%s/repos";
    private static final String REPO_BRANCHES_URL = "https://api.github.com/repos/%s/%s/branches";

/*    List<UsersRepos> findAll(String username){
        return urepos;
    }*/

    public RepoListerService(RestTemplate rest) {
        this.rest = rest;
    }

    public List<UsersRepos> findAll(String username) {
        try{
            String url = String.format(USER_REPOS_URL, username);
            ParameterizedTypeReference<List<Map<String, Object>>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<List<Map<String, Object>>> response = rest.exchange(url, HttpMethod.GET, null, responseType);
            List<Map<String, Object>> repos = response.getBody();

            List<GHRepository> ghRepositories = repos.stream()
                    .filter(repo -> !Boolean.TRUE.equals(repo.get("fork")))
                    .map(repo -> {
                        String repoName = (String) repo.get("name");
                        String branchesUrl = String.format(REPO_BRANCHES_URL, username, repoName);
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
        }
        catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User not found.");
            }
            else{
                throw exception;
            }
        }
    }
}
