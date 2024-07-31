package com.example.RepoLister.repositories;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/repos")
public class RepoListerController {
    private final RepoListerService repoListerService;

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<List<UsersRepos>> findAll(@PathVariable String username) {
        try {
            List<UsersRepos> repos = repoListerService.findAll(username);
            return ResponseEntity.ok(repos);
        } catch (GlobalControllerExceptionHandler exception) {
            throw exception;
        }
    }
}
