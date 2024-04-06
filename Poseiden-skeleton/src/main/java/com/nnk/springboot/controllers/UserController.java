package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller class handling CRUD operations for users.
 */
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Handler for displaying the list of users.
     *
     * @param model The Model object to add attributes.
     * @return The view name for the list of users.
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    /**
     * Handler for displaying the form to add a new user.
     *
     * @param user The User object to be populated in the form.
     * @return The view name for the add user form.
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }

    /**
     * Handler for validating and saving a new user.
     *
     * @param user   The User object to be validated and saved.
     * @param result The BindingResult for validation errors.
     * @param model  The Model object to add attributes.
     * @return The view name to redirect to after saving the user.
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            model.addAttribute("users", userRepository.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    /**
     * Handler for displaying the form to update an existing user.
     *
     * @param id    The ID of the user to be updated.
     * @param model The Model object to add attributes.
     * @return The view name for the update user form.
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Handler for updating an existing user.
     *
     * @param id     The ID of the user to be updated.
     * @param user   The User object with updated information.
     * @param result The BindingResult for validation errors.
     * @param model  The Model object to add attributes.
     * @return The view name to redirect to after updating the user.
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }

    /**
     * Handler for deleting an existing user.
     *
     * @param id    The ID of the user to be deleted.
     * @param model The Model object to add attributes.
     * @return The view name to redirect to after deleting the user.
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }
}
