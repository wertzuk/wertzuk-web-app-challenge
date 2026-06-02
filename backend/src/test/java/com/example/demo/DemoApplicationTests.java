package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void calculate_WithValidAmount_Returns200AndJson() throws Exception {
		mockMvc.perform(get("/calculate")
				.param("amount", "234.23")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(14)))
				.andExpect(jsonPath("$[0].value").value(20000))
				.andExpect(jsonPath("$[0].count").value(1));
	}

	@Test
	void calculate_WithPreviousAmount_CalculatesDifferences() throws Exception {
		mockMvc.perform(get("/calculate")
				.param("amount", "150.00")
				.param("previousAmount", "100.00"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[1].value").value(10000))
				.andExpect(jsonPath("$[1].difference").value(0))
				.andExpect(jsonPath("$[2].value").value(5000))
				.andExpect(jsonPath("$[2].difference").value(1));
	}

}
