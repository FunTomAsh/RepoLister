package com.example.RepoLister.repositories;

import java.util.List;

public record UsersRepos(
        String userLogin,
        List<GHRepository> repositories
        ) {
}
