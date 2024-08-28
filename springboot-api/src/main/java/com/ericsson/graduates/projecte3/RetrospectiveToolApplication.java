package com.ericsson.graduates.projecte3;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestOperationsExtensionsKt;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RetrospectiveToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetrospectiveToolApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate()
	{
		RestTemplate rest = new RestTemplate();

		rest.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());

		return rest;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(myObjectMapper());
		return converter;
	}

	@Bean
	public ObjectMapper myObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		// This where you enable default enum feature
		mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
		// Enable mapping for LocalDateTime
		mapper.registerModule(new JavaTimeModule());

		return mapper;
	}
}
