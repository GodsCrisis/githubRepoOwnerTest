package com.example.recrutation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/githubRepoOwner")
public class GithubRepoOwnerController {
    private final GithubRepoOwnerClient githubRepoOwnerClient;

    public GithubRepoOwnerController(GithubRepoOwnerClient githubRepoOwnerClient) {
        this.githubRepoOwnerClient = githubRepoOwnerClient;
    }

    @GetMapping("/repos/{owner}")
    public ResponseEntity<?> getRepos(@PathVariable String owner) {
        try {
            List<githubRepoOwnerRepos> result = githubRepoOwnerClient.getUserRepos(owner);
            return ResponseEntity.ok(result);
        }catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of(
                            "status",e.getStatusCode().value(),
                            "message",e.getStatusText()
                    ));
        }
    }
}
