package com.example.recrutation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class GithubRepoOwnerSpringApplicationTests {
	@Autowired
	private GithubRepoOwnerClient githubRepoOwnerClient;
	@Test
	void happyPathTest() {
		List<githubRepoOwnerRepos> repos = githubRepoOwnerClient.getUserRepos("GodsCrisis");
		assertNotNull(repos);
		assertFalse(repos.isEmpty(), "The list of repositories should not be empty");
		repos.forEach(repo->{
			assertNotNull(repo.name(), "Repository name should not be null");
			assertNotNull(repo.ownerLogin(), "Owner login should not be null");
			assertFalse(repo.branches().isEmpty(), "Branches list should not be empty for each repository");
			repo.branches().forEach(branch -> {
				assertNotNull(branch.branchName(), "Branch name should not be null");
				assertNotNull(branch.lastCommitSha(), "Last commit SHA should not be null");
			});
		});
	}

}
