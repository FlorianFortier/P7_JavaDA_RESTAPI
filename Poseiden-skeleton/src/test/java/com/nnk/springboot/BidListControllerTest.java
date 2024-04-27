package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class BidListControllerTest {

	@MockBean
	private BidListService bidListService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testShowBidListPage() throws Exception {
		mockMvc.perform(get("/bidList/list"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("bidList/list"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("bids"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("currentUser"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testGetAddBidForm() throws Exception {
		mockMvc.perform(get("/bidList/add"))
				.andExpect(status().isOk())
				.andExpect(view().name("bidList/add"));
	}
	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testValidate_WithValidData() throws Exception {
		mockMvc.perform(post("/bidList/validate")
						.with(csrf())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("account", "Test Account")
						.param("type", "Test Type")
						.param("bidQuantity", "100.0")
						.param("askQuantity", "200.0"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/bidList/list"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN", "USER"})
	void testValidateBidController() throws Exception {
		BidList bid = new BidList("Test Account", "Test Type", 100.0);
		bid.setId(1);
		bid.setAccount("Test Account");
		when(bidListService.saveBid(any())).thenReturn(bid);

		mockMvc.perform(post("/bidList/validate")
						.with(csrf())
						.param("account", "Test Account")
						.param("type", "Test Type")
						.param("bidQuantity", "100.0"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/bidList/list"));
	}

	@Test
	@WithMockUser(username = "testUser", roles = {"ADMIN"})
	void testUpdateBid_WithValidData() throws Exception {
		BidList existingBid = new BidList("Test Account", "Test Type", 100.0);
		existingBid.setId(1);

		BidList updatedBid = new BidList("Updated Account", "Updated Type", 200.0);
		updatedBid.setId(1);

		when(bidListService.getBidByBidListId(1)).thenReturn(Optional.of(existingBid));
		mockMvc.perform(post("/bidList/update/1")
						.with(csrf())
						.param("account", "Updated Account")
						.param("type", "Updated Type")
						.param("bidQuantity", "200.0"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/bidList/list"));


		verify(bidListService).saveBid(argThat(bid -> bid.getAccount().equals("Updated Account")
				&& bid.getType().equals("Updated Type")
				&& bid.getBidQuantity().equals(200.0)));	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testDeleteBid() throws Exception {
		mockMvc.perform(get("/bidList/delete/{id}", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/bidList/list"));
	}

	@Test
	@WithMockUser(username = "Admin", roles={"ADMIN"})
	void testAddBidForm() throws Exception {
		mockMvc.perform(get("/bidList/add"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("bid"))
				.andExpect(view().name("bidList/add"));
	}

}
