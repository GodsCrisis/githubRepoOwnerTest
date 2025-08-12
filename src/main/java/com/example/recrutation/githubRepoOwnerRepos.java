package com.example.recrutation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record githubRepoOwnerRepos (
        boolean fork,
        @JsonProperty("Repository Name") String name,
        @JsonProperty("Owner Login") String ownerLogin,
        @JsonProperty("Branches") List<githubRepoOwnerBranch> branches
        )

{
    public record githubRepoOwnerBranch(
            @JsonProperty("name") String branchName,
            @JsonProperty("lastCommitSha") String lastCommitSha
    ){}
}

