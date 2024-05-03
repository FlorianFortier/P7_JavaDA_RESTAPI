package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TradeControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private TradeService tradeService;

	@MockBean
	private UserService userService;

	@BeforeEach
	void setUp() {
		TradeController tradeController = new TradeController(tradeService, userService);
		mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testHome() throws Exception {
		when(userService.getCurrentLoggedInUser()).thenReturn("Admin");
		when(tradeService.getAllTrades()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/trade/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("trade/list"))
				.andExpect(model().attributeExists("trades"))
				.andExpect(model().attributeExists("currentUser"));

		verify(userService, times(1)).getCurrentLoggedInUser();
		verify(tradeService, times(1)).getAllTrades();
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testAddTradeForm() throws Exception {
		mockMvc.perform(get("/trade/add"))
				.andExpect(status().isOk())
				.andExpect(view().name("trade/add"))
				.andExpect(model().attributeExists("trade"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testValidate() throws Exception {
		mockMvc.perform(post("/trade/validate")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("account", "Test Account")
						.param("type", "Test Type")
						.param("buyQuantity", "10.0"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/trade/list"));

		verify(tradeService, times(1)).saveTrade(any(Trade.class));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testShowUpdateForm() throws Exception {
		Trade trade = new Trade("Test Account", "Test Type");
		when(tradeService.getTradeById(1)).thenReturn(Optional.of(trade));

		mockMvc.perform(get("/trade/update/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("trade/update"))
				.andExpect(model().attributeExists("trade"));

		verify(tradeService, times(1)).getTradeById(1);
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testUpdateTrade() throws Exception {
		mockMvc.perform(post("/trade/update/{id}", 1)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("account", "Test Account")
						.param("type", "Test Type")
						.param("buyQuantity", "10.0"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/trade/list"));

		verify(tradeService, times(1)).saveTrade(any(Trade.class));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testDeleteTrade() throws Exception {
		mockMvc.perform(get("/trade/delete/{id}", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/trade/list"));

		verify(tradeService, times(1)).deleteTradeById(1);
	}
}
