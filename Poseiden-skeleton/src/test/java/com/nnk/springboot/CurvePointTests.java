package com.nnk.springboot;

import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CurveController.class)
public class CurvePointTests {

	private CurveController curveController;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CurvePointService curvePointService;

	@Autowired
	private CurvePointRepository curvePointRepository;

	@Test
	public void curvePointTest() {
		CurvePoint curvePoint = new CurvePoint(10, 10d, 30d);

		// Save
		curvePoint = curvePointRepository.save(curvePoint);
		Assert.assertNotNull(curvePoint.getId());
        Assert.assertEquals(10, (int) curvePoint.getCurveId());

		// Update
		curvePoint.setCurveId(20);
		curvePoint = curvePointRepository.save(curvePoint);
        Assert.assertEquals(20, (int) curvePoint.getCurveId());

		// Find
		List<CurvePoint> listResult = curvePointRepository.findAll();
        Assert.assertFalse(listResult.isEmpty());

		// Delete
		Integer id = curvePoint.getId();
		curvePointRepository.delete(curvePoint);
		Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
		Assert.assertFalse(curvePointList.isPresent());
	}
	@Test
	public void testHome() throws Exception {
		when(curvePointService.getAllCurvePoints()).thenReturn(Collections.emptyList());
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/list"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("curvePoint/list"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("curvePoints"));
		verify(curvePointService, times(1)).getAllCurvePoints();
	}
	@Test
	public void testAddCurveForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/add"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("curvePoint/add"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("curvePoint"));
	}

	@Test
	public void testValidate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.view().name("curvePoint/add"));
	}

	@Test
	public void testShowUpdateForm() throws Exception {
		when(curvePointService.getCurvePointById(1)).thenReturn(Optional.of(new CurvePoint()));
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/update/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("curvePoint/update"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("curvePoint"));
		verify(curvePointService, times(1)).getCurvePointById(1);
	}

	@Test
	public void testUpdateCurve() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.view().name("curvePoint/update"));
	}

	@Test
	public void testDeleteCurve() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));
		verify(curvePointService, times(1)).deleteCurvePointById(1);
	}
}
