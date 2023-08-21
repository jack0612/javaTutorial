package com.pokemonreview.api.WebClient.GitHub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

 

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GithubWebclientService {
    private static final String GITHUB_V3_MIME_TYPE = "application/vnd.github.v3+json";
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private static final String USER_AGENT = "Spring 5 WebClient";
    private static final Logger logger = LoggerFactory.getLogger(GithubWebclientService.class);

    private final WebClient webClient;

    @Autowired
    public GithubWebclientService(GithubAppProperties appProperties) {
        this.webClient = WebClient.builder()
                .baseUrl(GITHUB_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, GITHUB_V3_MIME_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                //https://www.baeldung.com/spring-webclient-filters
                //A filter can intercept, examine, and modify a client request (or response).
                //In Spring Reactive, filters are instances of the functional interface ExchangeFilterFunction. 
                //The filter function has two parameters: the ClientRequest to modify and the next ExchangeFilterFunction.
                //The helper class ExchangeFilterFunctions offers the basicAuthentication() filter function
                //which takes care of adding the authorization header to the request.
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(appProperties.getGithub().getUsername(),
                                appProperties.getGithub().getToken()))
                .filter(logRequest())
                .build();
    }


    @SuppressWarnings("deprecation")
	public Flux<GithubRepo> listGithubRepositories() {
         return webClient.get()
                .uri("/user/repos?sort={sortField}&direction={sortDirection}",
                        "updated", "desc")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(GithubRepo.class));
    }

    public Mono<GithubRepo> createGithubRepository(GithubRepoRequest createRepoRequest) {
        return webClient.post()
                .uri("/user/repos")
                .body(Mono.just(createRepoRequest), GithubRepoRequest.class)
                .retrieve()
                .bodyToMono(GithubRepo.class);
    }

    public Mono<GithubRepo> getGithubRepository(String owner, String repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(GithubRepo.class);
    }

    public Mono<GithubRepo> editGithubRepository(String owner, String repo, GithubRepoRequest editRepoRequest) {
        return webClient.patch()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .body(BodyInserters.fromObject(editRepoRequest))
                .retrieve()
                .bodyToMono(GithubRepo.class);
    }

    public Mono<Void> deleteGithubRepository(String owner, String repo) {
        return webClient.delete()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }
}
