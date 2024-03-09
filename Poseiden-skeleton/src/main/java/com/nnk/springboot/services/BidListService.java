package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BidListService {

    private final BidListRepository bidListRepository;

    @Autowired
    public BidListService(BidListRepository bidListRep) {
        this.bidListRepository = bidListRep;
    }

    /**
     * Get a list of all bids
     *
     * @return list of BidListModel containing all bid models
     */
    public List<BidList> getAllBids() {
        return bidListRepository.findAll();
    }

    /**
     * Check if an Id already exists
     *
     * @param id the bid ID
     * @return true if ID already exists
     */
    public boolean checkIfIdExists(int id) {
        return bidListRepository.existsById(id);
    }

    /**
     * Save a new Bid in the DB
     *
     * @param bidList the BidListModel to save
     */
    public void saveBid(BidList bidList) {
        bidListRepository.save(bidList);
    }

    /**
     * Delete an existent bid from the DB
     *
     * @param bidListId the bid ID
     */
    public void deleteBidById(int bidListId) {
        bidListRepository.deleteById(bidListId);
    }

    /**
     * Get a Bid model by ID
     *
     * @param bidListId the bid ID
     * @return BidListModel found with the ID
     */
    public Optional<BidList> getBidByBidListId(int bidListId) {
        return bidListRepository.findById(bidListId);
    }
}