package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import com.nnk.springboot.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller class handling CRUD operations for RuleNames.
 */
@Controller
public class RuleNameController {

    private final RuleNameService ruleNameService;
    private final UserService userService;

    /**
     * Constructor with RuleNameService and UserService injection.
     *
     * @param ruleNameService The RuleNameService instance to be injected.
     * @param userService     The UserService instance to be injected.
     */
    @Autowired
    public RuleNameController(RuleNameService ruleNameService, UserService userService) {
        this.ruleNameService = ruleNameService;
        this.userService = userService;
    }

    /**
     * Handler for displaying the list of rule names.
     *
     * @param model The Model object to add attributes.
     * @return The view name for the list of rule names.
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        String currentUser = userService.getCurrentLoggedInUser();

        List<RuleName> ruleNames = ruleNameService.getAllRuleNames();
        model.addAttribute("ruleNames", ruleNames);
        model.addAttribute("currentUser", currentUser);
        return "ruleName/list";
    }

    /**
     * Handler for displaying the form to add a new rule name.
     *
     * @param model The Model object to add attributes.
     * @return The view name for the add rule name form.
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    /**
     * Handler for validating and saving a new rule name.
     *
     * @param ruleName The RuleName object to be validated and saved.
     * @param result   The BindingResult for validation errors.
     * @param model    The Model object to add attributes.
     * @return The view name to redirect to after saving the rule name.
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleNameService.saveRuleName(ruleName);
        return "redirect:/ruleName/list";
    }

    /**
     * Handler for displaying the form to update an existing rule name.
     *
     * @param id    The ID of the rule name to be updated.
     * @param model The Model object to add attributes.
     * @return The view name for the update rule name form.
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<RuleName> ruleName = ruleNameService.getRuleNameById(id);
        ruleName.ifPresent(r -> model.addAttribute("ruleName", r));
        return "ruleName/update";
    }

    /**
     * Handler for updating an existing rule name.
     *
     * @param id       The ID of the rule name to be updated.
     * @param ruleName The RuleName object with updated information.
     * @param result   The BindingResult for validation errors.
     * @param model    The Model object to add attributes.
     * @return The view name to redirect to after updating the rule name.
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        ruleNameService.saveRuleName(ruleName);
        return "redirect:/ruleName/list";
    }

    /**
     * Handler for deleting an existing rule name.
     *
     * @param id The ID of the rule name to be deleted.
     * @return The view name to redirect to after deleting the rule name.
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.deleteRuleNameById(id);
        return "redirect:/ruleName/list";
    }
}
