package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RuleNameControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private RuleNameService ruleNameService;

	@MockBean
	private UserService userService;

	@BeforeEach
	void setUp() {
		RuleNameController ruleNameController = new RuleNameController(ruleNameService, userService);
		mockMvc = MockMvcBuilders.standaloneSetup(ruleNameController).build();
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testHome() throws Exception {
		when(userService.getCurrentLoggedInUser()).thenReturn("Admin");
		when(ruleNameService.getAllRuleNames()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/ruleName/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("ruleName/list"))
				.andExpect(model().attributeExists("ruleNames"))
				.andExpect(model().attributeExists("currentUser"));

		verify(userService, times(1)).getCurrentLoggedInUser();
		verify(ruleNameService, times(1)).getAllRuleNames();
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testAddRuleForm() throws Exception {
		mockMvc.perform(get("/ruleName/add"))
				.andExpect(status().isOk())
				.andExpect(view().name("ruleName/add"))
				.andExpect(model().attributeExists("ruleName"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testValidate() throws Exception {
		mockMvc.perform(post("/ruleName/validate")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("name", "Test Name")
						.param("description", "Test Description")
						.param("json", "Test JSON")
						.param("template", "Test Template")
						.param("sqlStr", "Test SQL Str")
						.param("sqlPart", "Test SQL Part"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/ruleName/list"));

		verify(ruleNameService, times(1)).saveRuleName(any(RuleName.class));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testShowUpdateForm() throws Exception {
		RuleName ruleName = new RuleName("Test Name", "Test Description", "Test JSON",
				"Test Template", "Test SQL Str", "Test SQL Part");
		when(ruleNameService.getRuleNameById(1)).thenReturn(Optional.of(ruleName));

		mockMvc.perform(get("/ruleName/update/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("ruleName/update"))
				.andExpect(model().attributeExists("ruleName"));

		verify(ruleNameService, times(1)).getRuleNameById(1);
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testUpdateRuleName() throws Exception {
		mockMvc.perform(post("/ruleName/update/{id}", 1)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("name", "Test Name")
						.param("description", "Test Description")
						.param("json", "Test JSON")
						.param("template", "Test Template")
						.param("sqlStr", "Test SQL Str")
						.param("sqlPart", "Test SQL Part"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/ruleName/list"));

		verify(ruleNameService, times(1)).saveRuleName(any(RuleName.class));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testDeleteRuleName() throws Exception {
		mockMvc.perform(get("/ruleName/delete/{id}", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/ruleName/list"));

		verify(ruleNameService, times(1)).deleteRuleNameById(1);
	}
}
