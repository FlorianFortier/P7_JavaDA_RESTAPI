package services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    @Test
    void testGetAllRuleNames() {
        // Given
        RuleName ruleName1 = new RuleName();
        RuleName ruleName2 = new RuleName();
        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(ruleName1, ruleName2));

        // When
        List<RuleName> result = ruleNameService.getAllRuleNames();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void testCheckIfIdExists() {
        // Given
        int id = 1;
        when(ruleNameRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = ruleNameService.checkIfIdExists(id);

        // Then
        assertTrue(result);
    }

    @Test
    void testSaveRuleName() {
        // Given
        RuleName ruleName = new RuleName();

        // When
        ruleNameService.saveRuleName(ruleName);

        // Then
        verify(ruleNameRepository, times(1)).save(ruleName);
    }

    @Test
    void testDeleteRuleNameById() {
        // Given
        int id = 1;

        // When
        ruleNameService.deleteRuleNameById(id);

        // Then
        verify(ruleNameRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetRuleNameById() {
        // Given
        int id = 1;
        RuleName ruleName = new RuleName();
        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(ruleName));

        // When
        Optional<RuleName> result = ruleNameService.getRuleNameById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(ruleName, result.get());
    }
}
