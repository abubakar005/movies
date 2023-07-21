package com.backbase.movies;

import com.backbase.movies.dto.AuthenticationRequest;
import com.backbase.movies.dto.AuthenticationResponse;
import com.backbase.movies.dto.CreateUserRequest;
import com.backbase.movies.util.AwardsInfoFileUploadUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoviesApplicationTests extends BaseTestConfiguration {

	private String BASE_URL = "http://localhost:%s/api/v1/%s";

	@LocalServerPort
	private int port;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeAll
	static void setUp () {
		mySQLContainer.start();
		AwardsInfoFileUploadUtil.loadBestPictureAwardsMap();
	}

	HttpClient httpClient = HttpClient.newHttpClient();
	ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@Test
	@Order(1)
	@DisplayName("This should return a fresh JWT token")
	void shouldReturnTokenAfterAuthentication() throws Exception {

		ClassPathResource scriptResource = new ClassPathResource("scripts-test.sql");
		String scriptContent = new String(FileCopyUtils.copyToByteArray(scriptResource.getInputStream()), StandardCharsets.UTF_8);
		jdbcTemplate.execute(scriptContent);

		AuthenticationRequest authenticationRequest = new AuthenticationRequest("superadmin", "superadmin");

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(String.format(BASE_URL, port, "user/authenticate")))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(authenticationRequest)))
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		AuthenticationResponse authenticationResponse = objectMapper.readValue(response.body(), AuthenticationResponse.class);
		assertEquals(200, response.statusCode());
	}

	@Test
	@DisplayName("This should create a new User")
	void shouldCreateNewUser() throws Exception {

		CreateUserRequest userRequest = new CreateUserRequest("TestUser", "TestUser", "testuser@gmail.com", "ROLE_USER");

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(String.format(BASE_URL, port, "user/create")))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer "+getToken())
				.POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userRequest)))
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(201, response.statusCode());
	}

	@Test
	@DisplayName("This should return movie has won the best picture award")
	void shouldReturnMovieHasWonBestPictureAward() throws Exception {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(String.format(BASE_URL, port, "movie/Hamlet/best-picture-award")))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer "+getToken())
				.GET()
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertEquals("'Hamlet' has won a best picture award", response.body());
	}

	private String getToken() throws Exception {
		AuthenticationRequest authenticationRequest = new AuthenticationRequest("superadmin", "superadmin");

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(String.format(BASE_URL, port, "user/authenticate")))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(authenticationRequest)))
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		AuthenticationResponse authenticationResponse = objectMapper.readValue(response.body(), AuthenticationResponse.class);
		return authenticationResponse.token();
	}
}
