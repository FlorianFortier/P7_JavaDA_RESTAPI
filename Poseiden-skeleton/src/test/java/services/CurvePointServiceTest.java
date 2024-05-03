package services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurvePointServiceTest {
    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointService;
    @Test
    void testGetAllCurvePoints() {
        // Given
        CurvePoint curvePoint1 = new CurvePoint();
        CurvePoint curvePoint2 = new CurvePoint();
        when(curvePointRepository.findAll()).thenReturn(Arrays.asList(curvePoint1, curvePoint2));

        // When
        List<CurvePoint> result = curvePointService.getAllCurvePoints();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void testSaveCurvePoint() {
        // Given
        CurvePoint curvePoint = new CurvePoint();

        // When
        curvePointService.saveCurvePoint(curvePoint);

        // Then
        verify(curvePointRepository, times(1)).save(curvePoint);
    }

    @Test
    void testCheckIfIdExists() {
        // Given
        int id = 1;
        when(curvePointRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = curvePointService.checkIfIdExists(id);

        // Then
        assertTrue(result);
    }

    @Test
    void testDeleteCurvePointById() {
        // Given
        int id = 1;

        // When
        curvePointService.deleteCurvePointById(id);

        // Then
        verify(curvePointRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetCurvePointById() {
        // Given
        int id = 1;
        CurvePoint curvePoint = new CurvePoint();
        when(curvePointRepository.findById(id)).thenReturn(Optional.of(curvePoint));

        // When
        Optional<CurvePoint> result = curvePointService.getCurvePointById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(curvePoint, result.get());
    }

    @Test
    void testGetDateForFieldCreationDate() {
        // When
        LocalDateTime result = curvePointService.getDateForFieldCreationDate();

        // Then
        assertNotNull(result);
    }
}
