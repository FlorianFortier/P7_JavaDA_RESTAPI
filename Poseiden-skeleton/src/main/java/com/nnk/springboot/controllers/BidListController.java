package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class BidListController {

    private final BidListService bidListService;

    @Autowired
    public BidListController(BidListService bidListService) {
        this.bidListService = bidListService;
    }

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        List<BidList> bids = bidListService.getAllBids();
        model.addAttribute("bids", bids);
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        model.addAttribute("bid", new BidList());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.saveBid(bid);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> bid = bidListService.getBidByBidListId(id);
        bid.ifPresent(b -> model.addAttribute("bid", b));
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.saveBid(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidListService.deleteBidById(id);
        return "redirect:/bidList/list";
    }
}
