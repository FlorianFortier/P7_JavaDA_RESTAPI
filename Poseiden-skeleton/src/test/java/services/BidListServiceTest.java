package services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    @Test
    void testGetAllBids() {
        // Given
        BidList bid1 = new BidList();
        BidList bid2 = new BidList();
        when(bidListRepository.findAll()).thenReturn(Arrays.asList(bid1, bid2));

        // When
        List<BidList> result = bidListService.getAllBids();

        // Then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testCheckIfIdExists() {
        // Given
        int id = 1;
        when(bidListRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = bidListService.checkIfIdExists(id);

        // Then
        Assertions.assertTrue(result);
    }

    @Test
    void testSaveBid() {
        // Given
        BidList bid = new BidList();
        when(bidListRepository.save(bid)).thenReturn(bid);

        // When
        BidList result = bidListService.saveBid(bid);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bid, result);
    }

    @Test
    void testDeleteBidById() {
        // Given
        int id = 1;

        // When
        bidListService.deleteBidById(id);

        // Then
        verify(bidListRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetBidByBidListId() {
        // Given
        int id = 1;
        BidList bid = new BidList();
        when(bidListRepository.findById(id)).thenReturn(Optional.of(bid));

        // When
        Optional<BidList> result = bidListService.getBidByBidListId(id);

        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(bid, result.get());
    }
}
