package com.example.RepoLister.repositories;

import java.util.List;

public record GHRepository(
        String repositoryName,
        List<Branch> branches
) {
}
