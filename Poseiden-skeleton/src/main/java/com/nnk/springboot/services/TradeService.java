package com.nnk.springboot.services;


import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TradeService {

    private final TradeRepository tradeRepository;

    @Autowired
    public TradeService(TradeRepository tradeRep) {
        this.tradeRepository = tradeRep;
    }

    /**
     *  Check if an Id already exists
     * @param id the trade ID
     * @return true if ID already exists
     */
    public boolean checkIfTradeIdExists(int id) {
        return tradeRepository.existsById(id);
    }

    /**
     * Get a list of all trades
     *
     * @return list of TradeModel containing all trade models
     */
    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    /**
     * Save a new trade in the DB
     * @param trade the TradeModel to save
     */
    public void saveTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    /**
     * Delete an existent trade from the DB
     * @param id the trade ID
     */
    public void deleteTradeById(int id) {
        tradeRepository.deleteById(id);
    }

    /**
     * Get a trade model by ID
     *
     * @param id the trade ID
     * @return TradeModel found with the ID
     */
    public Optional<Trade> getTradeById(int id) {
        return tradeRepository.findById(id);
    }
    /**
     * Get a LocalDateTime for the field creationDate
     *
     * @return a LocalDateTime of the current time and date
     */
    public LocalDateTime getCreationDateForDateFields() {
        return LocalDateTime.now();
    }
}