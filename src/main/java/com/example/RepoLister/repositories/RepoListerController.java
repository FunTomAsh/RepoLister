package com.example.RepoLister.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepoListerController {

    private final RepoListerService repoListerService;
    @Autowired
    public RepoListerController(RepoListerService repoListerService) {
        this.repoListerService = repoListerService;
    }

    //{username}
    @GetMapping("/api/repos/{username}")
    List<UsersRepos> findAll(@PathVariable String username){
        return repoListerService.findAll(username);
    }
/*
    @GetMapping("/hello")
    String Searcher(){
        return "yo, world!";
    }
*/

    //

}
