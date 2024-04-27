package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling bid list operations.
 */
@Controller
public class BidListController {

    private final BidListService bidListService;
    private final UserService userService;
    private static final String REDIRECT_BIDLIST = "redirect:/bidList/list";

    /**
     * Constructor to initialize the BidListController with necessary services.
     *
     * @param bidListService The bid list service.
     * @param userService    The user service.
     */
    @Autowired
    public BidListController(BidListService bidListService, UserService userService) {
        this.bidListService = bidListService;
        this.userService = userService;
    }

    /**
     * Handler for displaying the bid list page.
     *
     * @param model The model to be populated with data.
     * @return The view name for the bid list page.
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        String currentUser = userService.getCurrentLoggedInUser();
        List<BidList> bids = bidListService.getAllBids();
        model.addAttribute("bids", bids);
        model.addAttribute("currentUser", currentUser);
        return "bidList/list";
    }

    /**
     * Handler for displaying the form to add a new bid.
     *
     * @param model The model to be populated with data.
     * @return The view name for the add bid form.
     */
    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        model.addAttribute("bid", new BidList());
        return "bidList/add";
    }

    /**
     * Handler for validating and adding a new bid.
     *
     * @param bid    The bid to be validated and added.
     * @param result The binding result for validation.
     * @return The view name for redirection after adding the bid.
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.saveBid(bid);
        return REDIRECT_BIDLIST;
    }

    /**
     * Handler for displaying the form to update an existing bid.
     *
     * @param id    The ID of the bid to be updated.
     * @param model The model to be populated with data.
     * @return The view name for the update bid form.
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> bid = bidListService.getBidByBidListId(id);
        bid.ifPresent(b -> model.addAttribute("bid", b));
        return "bidList/update";
    }

    /**
     * Handler for updating an existing bid.
     *
     * @param id      The ID of the bid to be updated.
     * @param bidList The updated bid information.
     * @param result  The binding result for validation.
     * @return The view name for redirection after updating the bid.
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        if (bidList == null) {
            bidList = new BidList(); // Créez un nouvel objet si aucun n'existe
        }
        bidListService.saveBid(bidList);
        return REDIRECT_BIDLIST;
    }

    /**
     * Handler for deleting an existing bid.
     *
     * @param id The ID of the bid to be deleted.
     * @return The view name for redirection after deleting the bid.
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidListService.deleteBidById(id);
        return REDIRECT_BIDLIST;
    }
}
