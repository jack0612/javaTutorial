package com.pokemonreview.api.WebClient.Twilio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TwilioClient {
	private final WebClient webClient;
	@Autowired
	private final TwilioAppProperties properties;

	public void sendSms(String from, String to, String message) {
        String baseUrl = properties.getBaseUrl();
        String accountSid = properties.getAccountSid();

        TwilioMessageRequest request = new TwilioMessageRequest(to, from, message);

        webClient.post()
                .uri(baseUrl + "/Accounts/{AccountSid}/Messages.json", accountSid)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TwilioMessageResponse.class)
                .blockOptional()
                .orElseThrow();
    }

	/*
	 * https://reflectoring.io/spring-webclient/
	 * 
	 * @GetMapping("/user/{id}") private Mono<User> getUserById(@PathVariable String
	 * id) { // Load some user data asynchronously, e.g. from a DB:
	 * Mono<BaseUserInfo> userInfo = getBaseUserInfo(id);
	 * 
	 * // Load user data with WebClient from a separate API: Mono<UserSubscription>
	 * userSubscription = client.get() .uri("http://subscription-service/api/user/"
	 * + id) .retrieve() .bodyToMono(UserSubscription.class);
	 * 
	 * // Combine the monos: when they are both done, take the // data from each and
	 * combine it into a User object. Mono<User> user = userInfo
	 * .zipWith(userSubscription) .map((tuple) -> new User(tuple.getT1(),
	 * tuple.getT2());
	 * 
	 * // The resulting mono of combined data can be returned immediately, //
	 * without waiting or blocking, and WebFlux will handle sending // the response
	 * later, once all the data is ready: return user; }
	 */
	 
}
