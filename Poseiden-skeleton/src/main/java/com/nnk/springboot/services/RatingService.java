package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRep) {
        this.ratingRepository = ratingRep;
    }

    /**
     * Get a list of all ratings
     *
     * @return list of RatingModel containing all rating models
     */
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    /**
     *  Check if an Id already exists
     * @param id the rating ID
     * @return true if ID already exists
     */
    public boolean checkIfIdExists(int id) {
        return ratingRepository.existsById(id);
    }

    /**
     * Save a new rating in the DB
     * @param rating the BidListModel to save
     */
    public void saveRating(Rating rating) {
        ratingRepository.save(rating);
    }

    /**
     * Delete an existent rating from the DB
     * @param id the rating ID
     */
    public void deleteRatingById(int id) {
        ratingRepository.deleteById(id);
    }

    /**
     * Get a rating model by ID
     *
     * @param id the rating ID
     * @return RatingModel found with the ID
     */
    public Optional<Rating> getRatingById(int id) {
        return ratingRepository.findById(id);
    }
}