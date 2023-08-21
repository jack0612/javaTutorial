package com.pokemonreview.api.WebClient.GitHub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

 


@JsonInclude(JsonInclude.Include.NON_NULL)
public class GithubRepoRequest {
    @jakarta.validation.constraints.NotBlank
    private String name;

    private String description;

    @JsonProperty("private")
    private Boolean isPrivate;


    public GithubRepoRequest() {

    }

    public GithubRepoRequest(@jakarta.validation.constraints.NotBlank String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
