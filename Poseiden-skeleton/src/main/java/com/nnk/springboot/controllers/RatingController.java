package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
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
 * Controller class handling CRUD operations for ratings.
 */
@Controller
public class RatingController {

    private final RatingService ratingService;
    private final UserService userService;

    /**
     * Constructor with RatingService and UserService injection.
     *
     * @param ratingService The RatingService instance to be injected.
     * @param userService   The UserService instance to be injected.
     */
    @Autowired
    public RatingController(RatingService ratingService, UserService userService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }

    /**
     * Handler for displaying the list of ratings.
     *
     * @param model The Model object to add attributes.
     * @return The view name for the list of ratings.
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        String currentUser = userService.getCurrentLoggedInUser();
        List<Rating> ratings = ratingService.getAllRatings();
        model.addAttribute("ratings", ratings);
        model.addAttribute("currentUser", currentUser);
        return "rating/list";
    }

    /**
     * Handler for displaying the form to add a new rating.
     *
     * @param model The Model object to add attributes.
     * @return The view name for the add rating form.
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    /**
     * Handler for validating and saving a new rating.
     *
     * @param rating The Rating object to be validated and saved.
     * @param result The BindingResult for validation errors.
     * @param model  The Model object to add attributes.
     * @return The view name to redirect to after saving the rating.
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingService.saveRating(rating);
        return "redirect:/rating/list";
    }

    /**
     * Handler for displaying the form to update an existing rating.
     *
     * @param id    The ID of the rating to be updated.
     * @param model The Model object to add attributes.
     * @return The view name for the update rating form.
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Rating> rating = ratingService.getRatingById(id);
        rating.ifPresent(r -> model.addAttribute("rating", r));
        return "rating/update";
    }

    /**
     * Handler for updating an existing rating.
     *
     * @param id     The ID of the rating to be updated.
     * @param rating The Rating object with updated information.
     * @param result The BindingResult for validation errors.
     * @param model  The Model object to add attributes.
     * @return The view name to redirect to after updating the rating.
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        ratingService.saveRating(rating);
        return "redirect:/rating/list";
    }

    /**
     * Handler for deleting an existing rating.
     *
     * @param id The ID of the rating to be deleted.
     * @return The view name to redirect to after deleting the rating.
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteRatingById(id);
        return "redirect:/rating/list";
    }
}
