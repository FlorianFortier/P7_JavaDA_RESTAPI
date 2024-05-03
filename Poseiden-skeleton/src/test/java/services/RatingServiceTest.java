package services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void testGetAllRatings() {
        // Given
        Rating rating1 = new Rating();
        Rating rating2 = new Rating();
        when(ratingRepository.findAll()).thenReturn(Arrays.asList(rating1, rating2));

        // When
        List<Rating> result = ratingService.getAllRatings();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void testCheckIfIdExists() {
        // Given
        int id = 1;
        when(ratingRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = ratingService.checkIfIdExists(id);

        // Then
        assertTrue(result);
    }

    @Test
    void testSaveRating() {
        // Given
        Rating rating = new Rating();

        // When
        ratingService.saveRating(rating);

        // Then
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    void testDeleteRatingById() {
        // Given
        int id = 1;

        // When
        ratingService.deleteRatingById(id);

        // Then
        verify(ratingRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetRatingById() {
        // Given
        int id = 1;
        Rating rating = new Rating();
        when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));

        // When
        Optional<Rating> result = ratingService.getRatingById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(rating, result.get());
    }
}
