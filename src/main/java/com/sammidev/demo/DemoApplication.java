package com.sammidev.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		GreetingWebClient greetingWebClient = new GreetingWebClient();
		System.out.println(greetingWebClient.getResult());
	}
}

@Component
class GreeetingHandler {

	public Mono<ServerResponse> sayHello(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromValue("hello Sammidev"));
	}
}

@Configuration
class GreetingRouter{

	@Bean
	public RouterFunction<ServerResponse> route(GreeetingHandler greetingHandler) {
		return RouterFunctions
				.route(RequestPredicates.GET("/hello")
								.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),greetingHandler::sayHello);
	}
}

class GreetingWebClient {
	private WebClient webClient = WebClient.create("http://localhost:8080");
	private Mono<ClientResponse> result = webClient.get()
			.uri("/hello")
			.accept(MediaType.TEXT_PLAIN)
			.exchange();

	public String getResult() {
		return  ">> result = " + result.flatMap(res -> res.bodyToMono(String.class)).block();
	}
}

