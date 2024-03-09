package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CurvePointService {

    private final CurvePointRepository curvePointRep;

    @Autowired
    public CurvePointService(CurvePointRepository curvePointRep) {
        this.curvePointRep = curvePointRep;
    }

    /**
     * Get a list of all curve points
     *
     * @return list of CurvePointModel containing all curve point models
     */
    public List<CurvePoint> getAllCurvePoints() {
        return curvePointRep.findAll();
    }

    /**
     * Save a new curve point in the DB
     * @param curvePoint the CurvePointModel to save
     */
    public void saveCurvePoint(CurvePoint curvePoint) {
        curvePointRep.save(curvePoint);
    }

    /**
     * Check if an Id already exists
     * @param id the curve point ID
     * @return true if ID already exists
     */
    public boolean checkIfIdExists(int id) {
        return curvePointRep.existsById(id);
    }

    /**
     * Delete an existent Curve Point from the DB
     * @param id the curve point ID
     */
    public void deleteCurvePointById(int id) {
        curvePointRep.deleteById(id);
    }

    /**
     * Get a Curve Point model by ID
     *
     * @param curvePointId the curve point ID
     * @return CurvePointModel found with the ID
     */
    public Optional<CurvePoint> getCurvePointById(int curvePointId) {
        return curvePointRep.findById(curvePointId);
    }

    /**
     * Get a LocalDateTime for the field creationDate
     *
     * @return a LocalDateTime of the current time and date
     */
    public LocalDateTime getDateForFieldCreationDate(){
        return LocalDateTime.now();
    }
}