package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @RequestMapping("/rating/list")
    public String home(Model model) {
        List<Rating> ratings = ratingService.getAllRatings();
        model.addAttribute("ratings", ratings);
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingService.saveRating(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Rating> rating = ratingService.getRatingById(id);
        rating.ifPresent(r -> model.addAttribute("rating", r));
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        ratingService.saveRating(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteRatingById(id);
        return "redirect:/rating/list";
    }
}
