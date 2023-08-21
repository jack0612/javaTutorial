package com.pokemonreview.api.WebClient;
//https://www.arhohuttunen.com/spring-boot-webclient-mockwebserver/

import okhttp3.mockwebserver.MockResponse;
import org.springframework.test.web.reactive.server.WebTestClient;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.pokemonreview.api.WebClient.Twilio.TwilioClient;
import com.pokemonreview.api.WebClient.Twilio.TwilioAppProperties;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest
@AutoConfigureMockMvc
public class WebClientTest {
	private final BasicJsonTester json = new BasicJsonTester(this.getClass());
	private MockWebServer mockWebServer;
	private TwilioClient twilioClient;

	@BeforeEach
	void setupMockWebServer() {
		mockWebServer = new MockWebServer();

		TwilioAppProperties properties = new TwilioAppProperties();
		properties.setBaseUrl(mockWebServer.url("/").url().toString());
		properties.setAccountSid("ACd936ed6dc1504dd79530f19f57b9c008");

		twilioClient = new TwilioClient(WebClient.create(), properties);
	}

	@Test
	void makesTheCorrectRequest() throws InterruptedException {
		mockWebServer.enqueue(new MockResponse().setResponseCode(200)
				.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.setBody(getJson("message-response.json")));

		twilioClient.sendSms("+123456", "+234567", "test message");

		RecordedRequest request = mockWebServer.takeRequest();

		assertEquals(request.getMethod(), "POST");
		assertEquals(request.getPath(), "/Accounts/ACd936ed6dc1504dd79530f19f57b9c008/Messages.json");
	}

	@Test
	void serializesRequest() throws InterruptedException {
		mockWebServer.enqueue(new MockResponse().setResponseCode(200)
				.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.setBody(getJson("message-response.json")));

		twilioClient.sendSms("+123456", "+234567", "test message");

		RecordedRequest request = mockWebServer.takeRequest();
		JsonContent<Object> body = json.from(request.getBody().readUtf8());

//        assertEquals(body.extractingJsonPathStringValue("$.from"),"+123456");
//        assertEquals(body.extractingJsonPathStringValue("$.to","+234567");
//        assertEquals(body.extractingJsonPathStringValue("$.body","test message");

	}

	private String getJson(String path) {
		try {
			InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream(path);
			assert jsonStream != null;
			return new String(jsonStream.readAllBytes());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	// https://reflectoring.io/spring-webclient/
	public void ResponseSpec() throws URISyntaxException {
		WebClient client = WebClient.create();
		// As a reactive API, the request is not actually sent until something attempts
		// to read or wait for the response.
		// we called .retrieve() to get a ResponseSpec for a request.
		WebClient.ResponseSpec responseSpec = client.get().uri("http://example.com").retrieve();
		String responseBody = responseSpec.bodyToMono(String.class)
				// we’ll use the simplest traditional option, by blocking to wait for the data
				// to arrive:
				.block();

		MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();

		bodyValues.add("key", "value");
		bodyValues.add("another-key", "another-value");

		String response = client.post().uri(new URI("https://httpbin.org/post"))
				.header("Authorization", "Bearer MY_SECRET_TOKEN").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromFormData(bodyValues)).retrieve()
				.bodyToMono(String.class).block();

		ResponseEntity<String> responseEntity = client.get().uri("https://httpbin.org/post").retrieve()
				// Don't treat 401 responses as errors:
				.onStatus(status -> status == HttpStatus.NOT_FOUND, clientResponse -> Mono.empty())
				.toEntity(String.class).block();
		HttpHeaders responseHeaders = responseEntity.getHeaders();
		List<String> headerValue = responseHeaders.get("header-name");
		if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
			// ...
		} else {
			// ...
		}
	}

	@Test
	public void WebTestClient(@Autowired WebTestClient webClient) {
		webClient.get().uri("/").exchange().expectStatus().isOk().expectBody(String.class).isEqualTo("Hello World");
		// https://reflectoring.io/spring-webclient/
		// bound to a server and sending real requests over HTTP,
		WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:8000").build();
		// Or connect to a single WebHandler using mock objects:
//		WebTestClient client2 = WebTestClient
//		    .bindToWebHandler(handler)
//		    .build();
		client.get().uri("/api/user/123").exchange().expectStatus().isNotFound(); // Assert that this is a 404 response
	}
}
