package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameService {

    private RuleNameRepository ruleNameRepository;

    @Autowired
    public RuleNameService(RuleNameRepository ruleNameRep) {
        this.ruleNameRepository = ruleNameRep;
    }

    /**
     * Get a list of all rule names
     *
     * @return list of rule names containing all rule name models
     */
    public List<RuleName> getAllRuleNames() {
        return ruleNameRepository.findAll();
    }

    /**
     * Check if an Id already exists
     *
     * @param id the rule name ID
     * @return true if ID already exists
     */
    public boolean checkIfIdExists(int id) {
        return ruleNameRepository.existsById(id);
    }

    /**
     * Save a new rule name in the DB
     *
     * @param ruleName the RuleNameModel to save
     */
    public void saveRuleName(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    /**
     * Delete an existent rule name from the DB
     *
     * @param id the rule name ID
     */
    public void deleteRuleNameById(int id) {
        ruleNameRepository.deleteById(id);
    }

    /**
     * Get a rule name model by ID
     *
     * @param id the rule name ID
     * @return RuleNameModel found with the ID
     */
    public Optional<RuleName> getRuleNameById(int id) {
        return ruleNameRepository.findById(id);
    }
}