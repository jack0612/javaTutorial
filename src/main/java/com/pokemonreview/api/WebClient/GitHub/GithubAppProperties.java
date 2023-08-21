package com.pokemonreview.api.WebClient.GitHub;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class GithubAppProperties {
	 private final Github github = new Github();
	 //Static class in Java is a nested class 
	 //Nested class can be static or non-static. The non-static nested class is known as an inner class.
	 //An instance of a static nested class can be created without the instance of the outer class.
	    public static class Github {
	        private String username;
	        private String token;

	        public String getUsername() {
	            return username;
	        }

	        public void setUsername(String username) {
	            this.username = username;
	        }

	        public String getToken() {
	            return token;
	        }

	        public void setToken(String token) {
	            this.token = token;
	        }
	    }

	    public Github getGithub() {
	        return github;
	    }
}
