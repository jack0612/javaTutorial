package com.pokemonreview.api.WebClient.GitHub;

 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

 


@RestController
@RequestMapping("/api")
public class GithubController {

    @Autowired
    private GithubWebclientService githubClient;

    @Autowired
    private GithubAppProperties appProperties;

    private static final Logger logger = LoggerFactory.getLogger(GithubController.class);

    @GetMapping("/repos")
    public Flux<GithubRepo> listGithubRepositories() {
        return githubClient.listGithubRepositories();
    }

    @PostMapping("/repos")
    public Mono<GithubRepo> createGithubRepository(@RequestBody GithubRepoRequest repoRequest) {
        return githubClient.createGithubRepository(repoRequest);
    }

    @GetMapping("/repos/{repo}")
    public Mono<GithubRepo> getGithubRepository(@PathVariable String repo) {
        return githubClient.getGithubRepository(appProperties.getGithub().getUsername(), repo);
    }

    @PatchMapping("/repos/{repo}")
    public Mono<GithubRepo> editGithubRepository(@PathVariable String repo, @Valid @RequestBody GithubRepoRequest repoRequest) {
        return githubClient.editGithubRepository(appProperties.getGithub().getUsername(), repo, repoRequest);
    }

    @DeleteMapping("/repos/{repo}")
    public Mono<Void> deleteGithubRepository(@PathVariable String repo) {
        return githubClient.deleteGithubRepository(appProperties.getGithub().getUsername(), repo);
    }


    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        logger.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(),
                ex.getResponseBodyAsString(), ex);
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
