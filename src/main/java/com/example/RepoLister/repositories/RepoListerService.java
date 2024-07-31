package com.example.RepoLister.repositories;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RepoListerService {

    private final RestTemplate rest;

    public List<UsersRepos> findAll(String username) {
        try{
            String url = String.format("https://api.github.com/users/%s/repos", username);
            ParameterizedTypeReference<List<Map<String, Object>>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<List<Map<String, Object>>> response = rest.exchange(url, HttpMethod.GET, null, responseType);
            List<Map<String, Object>> repos = response.getBody();

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
