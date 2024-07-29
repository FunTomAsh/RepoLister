package com.example.RepoLister.repositories;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RepoListerService {

    private List<UsersRepos> urepos = new ArrayList<>();
    private final RestTemplate rest;

/*    List<UsersRepos> findAll(String username){
        return urepos;
    }*/

    public RepoListerService(RestTemplate rest) {
        this.rest = rest;
    }

    public List<UsersRepos> findAll(String username) {
        try{
            String url = String.format("https://api.github.com/users/%s/repos", username);
            List<Map<String, Object>> repos = rest.getForObject(url, List.class);

            List<GHRepository> ghRepositories = new ArrayList<>();
            for (Map<String, Object> repo : repos) {
                if (Boolean.TRUE.equals(repo.get("fork"))) {
                    continue;
                }
                String repoName = (String) repo.get("name");
                String branchesUrl = String.format("https://api.github.com/repos/%s/%s/branches", username, repoName);
                List<Map<String, Object>> branches = rest.getForObject(branchesUrl, List.class);

                List<Branch> branchList = new ArrayList<>();
                for (Map<String, Object> branch : branches) {
                    String branchName = (String) branch.get("name");
                    String lastCommitSha = (String) ((Map<String, Object>) branch.get("commit")).get("sha");
                    branchList.add(new Branch(branchName, lastCommitSha));
                }
                ghRepositories.add(new GHRepository(repoName, branchList));
            }

            List<UsersRepos> usersReposList = new ArrayList<>();
            usersReposList.add(new UsersRepos(username, ghRepositories));
            return usersReposList;
        }
        catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User not found.");
            }
            throw exception;
        }
    }
/*    @PostConstruct
    private void init(){
        urepos.add(
                new UsersRepos(
                        username,
                        Collections.singletonList( new GHRepository("RepoLister", Collections.singletonList(new Branch("Branch1", "CommitLast"))
        ))));
    }*/

}
