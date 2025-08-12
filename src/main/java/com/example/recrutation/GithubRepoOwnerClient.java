package com.example.recrutation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Component
public class GithubRepoOwnerClient {
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://api.github.com";
    public GithubRepoOwnerClient() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getInterceptors().add(((request, body, execution) -> {
            request.getHeaders().add("User-Agent","MySpringApp");
            return execution.execute(request, body);
        }));
    }
    public List<githubRepoOwnerRepos> getUserRepos(String owner) {
        try{
            String url= BASE_URL +"/users/"+owner+"/repos";
            githubRepoResponse[] repos = restTemplate.getForObject(url, githubRepoResponse[].class);
            if (repos == null) {
                return List.of();
            }
             return Arrays.stream(repos).filter(repo->!repo.fork()).map(repo->{
                 List<githubRepoOwnerRepos.githubRepoOwnerBranch> branches= getBranches(repo.owner.login(), repo.name());
                    return new githubRepoOwnerRepos(
                            repo.fork(),
                            repo.name,
                            repo.owner.login,
                            branches
                    );
             }).toList();

        }catch(HttpClientErrorException e){
            throw e;
        }
    }

    private List<githubRepoOwnerRepos.githubRepoOwnerBranch> getBranches(String owenr, String repo){
        BranchResponse[] branches = restTemplate.getForObject(
                BASE_URL + "/repos/" + owenr + "/" + repo + "/branches",
                BranchResponse[].class
        );
        if(branches == null) return List.of();
        return Arrays.stream(branches)
                .map(branch -> new githubRepoOwnerRepos.githubRepoOwnerBranch(branch.name, branch.commit.sha))
                .toList();
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record BranchResponse(
            String name,
            Commit commit
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Commit(
                String sha
        ) {}
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record githubRepoResponse(
            boolean fork,
            String name,
            Owner owner
    ){
        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Owner(
                String login
        ) {}
    }
}
