package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RatingTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RatingService ratingService;

	@MockBean
	private UserService userService;

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testHome() throws Exception {
		when(userService.getCurrentLoggedInUser()).thenReturn("Admin");
		when(ratingService.getAllRatings()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/rating/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("rating/list"))
				.andExpect(model().attributeExists("ratings"))
				.andExpect(model().attributeExists("currentUser"));

		verify(userService, times(1)).getCurrentLoggedInUser();
		verify(ratingService, times(1)).getAllRatings();
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testAddRatingForm() throws Exception {
		mockMvc.perform(get("/rating/add"))
				.andExpect(status().isOk())
				.andExpect(view().name("rating/add"))
				.andExpect(model().attributeExists("rating"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testValidate() throws Exception {
		mockMvc.perform(post("/rating/validate")
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("moodysRating", "Test")
						.param("sandPRating", "Test")
						.param("fitchRating", "Test")
						.param("orderNumber", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/rating/list"));

		verify(ratingService, times(1)).saveRating(any(Rating.class));
	}
	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testShowUpdateForm() throws Exception {
		Rating rating = new Rating("Test", "Test", "Test", 1);
		when(ratingService.getRatingById(1)).thenReturn(Optional.of(rating));

		mockMvc.perform(get("/rating/update/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("rating/update"))
				.andExpect(model().attributeExists("rating"));

		verify(ratingService, times(1)).getRatingById(1);
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testUpdateRating() throws Exception {
		mockMvc.perform(post("/rating/update/{id}", 1)
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("moodysRating", "Test")
						.param("sandPRating", "Test")
						.param("fitchRating", "Test")
						.param("orderNumber", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/rating/list"));

		verify(ratingService, times(1)).saveRating(any(Rating.class));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testDeleteRating() throws Exception {
		mockMvc.perform(get("/rating/delete/{id}", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/rating/list"));

		verify(ratingService, times(1)).deleteRatingById(1);
	}
}
