package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import com.nnk.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling curve point operations.
 */
@Controller
public class CurveController {

    private final CurvePointService curvePointService;
    private final UserService userService;

    /**
     * Constructor to initialize the CurveController with necessary services.
     *
     * @param curvePointService The curve point service.
     * @param userService       The user service.
     */
    @Autowired
    public CurveController(CurvePointService curvePointService, UserService userService) {
        this.curvePointService = curvePointService;
        this.userService = userService;
    }

    /**
     * Handler for displaying the curve point list page.
     *
     * @param model The model to be populated with data.
     * @return The view name for the curve point list page.
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        String currentUser = userService.getCurrentLoggedInUser();
        List<CurvePoint> curvePoints = curvePointService.getAllCurvePoints();
        model.addAttribute("curvePoints", curvePoints);
        model.addAttribute("currentUser", currentUser);
        return "curvePoint/list";
    }

    /**
     * Handler for displaying the form to add a new curve point.
     *
     * @param model The model to be populated with data.
     * @return The view name for the add curve point form.
     */
    @GetMapping("/curvePoint/add")
    public String addCurveForm(Model model) {
        model.addAttribute("curvePoint", new CurvePoint());
        return "curvePoint/add";
    }

    /**
     * Handler for validating and adding a new curve point.
     *
     * @param curvePoint The curve point to be validated and added.
     * @param result     The binding result for validation.
     * @param model      The model to be populated with data.
     * @return The view name for redirection after adding the curve point.
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.saveCurvePoint(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Handler for displaying the form to update an existing curve point.
     *
     * @param id    The ID of the curve point to be updated.
     * @param model The model to be populated with data.
     * @return The view name for the update curve point form.
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<CurvePoint> curvePoint = curvePointService.getCurvePointById(id);
        curvePoint.ifPresent(cp -> model.addAttribute("curvePoint", cp));
        return "curvePoint/update";
    }

    /**
     * Handler for updating an existing curve point.
     *
     * @param id          The ID of the curve point to be updated.
     * @param curvePoint  The updated curve point information.
     * @param result      The binding result for validation.
     * @param model       The model to be populated with data.
     * @return The view name for redirection after updating the curve point.
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurve(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.saveCurvePoint(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Handler for deleting an existing curve point.
     *
     * @param id The ID of the curve point to be deleted.
     * @return The view name for redirection after deleting the curve point.
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id) {
        curvePointService.deleteCurvePointById(id);
        return "redirect:/curvePoint/list";
    }
}
