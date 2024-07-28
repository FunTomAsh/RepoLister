package com.example.RepoLister.repositories;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class RepoListerService {

    private List<UsersRepos> urepos = new ArrayList<>();

    List<UsersRepos> findAll(String username){
        urepos.add(
                new UsersRepos(
                        username,
                        Collections.singletonList( new GHRepository("RepoLister", Collections.singletonList(new Branch("Branch1", "CommitLast"))
        ))));
        return urepos;
    }
/*    @PostConstruct
    private void init(){
        urepos.add(
                new UsersRepos("TomashKarpei", "RepoLister", Collections.singletonList(new Branch("B1", "lastComm")))
        );
        urepos.add(
                new UsersRepos("TK", "RL", Collections.singletonList(new Branch("B2", "lastComm?")))
        );
    }*/

}
