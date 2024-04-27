package com.nnk.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CurveControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CurvePointService curvePointService;

	@MockBean
	private UserService userService;

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testHome() throws Exception {
		// Mocking userService.getCurrentLoggedInUser()
		when(userService.getCurrentLoggedInUser()).thenReturn("Admin");

		// Mocking curvePointService.getAllCurvePoints()
		when(curvePointService.getAllCurvePoints()).thenReturn(Collections.emptyList());

		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("curvePoint/list"))
				.andExpect(model().attributeExists("curvePoints"))
				.andExpect(model().attributeExists("currentUser"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testAddCurveForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/add"))
				.andExpect(status().isOk())
				.andExpect(view().name("curvePoint/add"))
				.andExpect(model().attributeExists("curvePoint"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testValidate() throws Exception {
		CurvePoint curvePoint = new CurvePoint();
		curvePoint.setCurveId(1);
		curvePoint.setTerm(10.0);
		curvePoint.setValue(20.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
						.with(csrf())
						.content(asJsonString(curvePoint))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/curvePoint/list"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testUpdateCurve() throws Exception {
		CurvePoint curvePoint = new CurvePoint();
		curvePoint.setId(1);
		curvePoint.setCurveId(1);
		curvePoint.setTerm(10.0);
		curvePoint.setValue(20.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/{id}", 1)
						.with(csrf())
						.content(asJsonString(curvePoint))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/curvePoint/list"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testDeleteCurve() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/{id}", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/curvePoint/list"));

		// Vérification que le service a bien été appelé avec l'ID en argument
		verify(curvePointService, times(1)).deleteCurvePointById(1);
	}

	// Méthode utilitaire pour convertir un objet en JSON
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
