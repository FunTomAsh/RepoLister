package com.example.RepoLister.repositories;

public record Branch(
        String branchName,
        String lastCommSHA
) {
}
