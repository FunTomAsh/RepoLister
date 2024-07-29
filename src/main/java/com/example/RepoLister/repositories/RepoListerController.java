package com.example.RepoLister.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repos")
public class RepoListerController {

    private final RepoListerService repoListerService;
    @Autowired
    public RepoListerController(RepoListerService repoListerService) {
        this.repoListerService = repoListerService;
    }

    //{username}
    @GetMapping("/{username}")
    public ResponseEntity<List<UsersRepos>> findAll(@PathVariable String username) {
        try {
            List<UsersRepos> repos = repoListerService.findAll(username);
            return ResponseEntity.ok(repos);
        } catch (GlobalControllerExceptionHandler exception) {
            throw exception;
        }
    }
/*
    @GetMapping("/hello")
    String Searcher(){
        return "yo, world!";
    }
*/
}
