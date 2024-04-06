package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
 * Controller class handling CRUD operations for trades.
 */
@Controller
public class TradeController {

    private final TradeService tradeService;
    private final UserService userService;

    /**
     * Constructor with TradeService and UserService injection.
     *
     * @param tradeService The TradeService instance to be injected.
     * @param userService  The UserService instance to be injected.
     */
    @Autowired
    public TradeController(TradeService tradeService, UserService userService) {
        this.tradeService = tradeService;
        this.userService = userService;
    }

    /**
     * Handler for displaying the list of trades.
     *
     * @param model The Model object to add attributes.
     * @return The view name for the list of trades.
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {
        String currentUser = userService.getCurrentLoggedInUser();

        List<Trade> trades = tradeService.getAllTrades();
        model.addAttribute("trades", trades);
        model.addAttribute("currentUser", currentUser);
        return "trade/list";
    }

    /**
     * Handler for displaying the form to add a new trade.
     *
     * @param model The Model object to add attributes.
     * @return The view name for the add trade form.
     */
    @GetMapping("/trade/add")
    public String addTradeForm(Model model) {
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    /**
     * Handler for validating and saving a new trade.
     *
     * @param trade  The Trade object to be validated and saved.
     * @param result The BindingResult for validation errors.
     * @param model  The Model object to add attributes.
     * @return The view name to redirect to after saving the trade.
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.saveTrade(trade);
        return "redirect:/trade/list";
    }

    /**
     * Handler for displaying the form to update an existing trade.
     *
     * @param id    The ID of the trade to be updated.
     * @param model The Model object to add attributes.
     * @return The view name for the update trade form.
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Trade> trade = tradeService.getTradeById(id);
        trade.ifPresent(t -> model.addAttribute("trade", t));
        return "trade/update";
    }

    /**
     * Handler for updating an existing trade.
     *
     * @param id     The ID of the trade to be updated.
     * @param trade  The Trade object with updated information.
     * @param result The BindingResult for validation errors.
     * @param model  The Model object to add attributes.
     * @return The view name to redirect to after updating the trade.
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        tradeService.saveTrade(trade);
        return "redirect:/trade/list";
    }

    /**
     * Handler for deleting an existing trade.
     *
     * @param id The ID of the trade to be deleted.
     * @return The view name to redirect to after deleting the trade.
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.deleteTradeById(id);
        return "redirect:/trade/list";
    }
}
